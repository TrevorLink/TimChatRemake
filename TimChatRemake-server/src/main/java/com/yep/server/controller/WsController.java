package com.yep.server.controller;

import com.github.binarywang.java.emoji.EmojiConverter;
import com.yep.server.pojo.GroupMsgContent;
import com.yep.server.pojo.Message;
import com.yep.server.pojo.User;
import com.yep.server.service.GroupMsgContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 聊天的controller
 *
 * @author HuangSir
 * @date 2022-02-15 16:16
 */
@Controller
@Slf4j
public class WsController {
   @Autowired
   private SimpMessagingTemplate simpMessagingTemplate;
   @Autowired
   private GroupMsgContentService groupMsgContentService;
//   @Autowired
//   private EmojiConverter emojiConverter;
   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

   /**
    * 群聊消息的接收和转发
    *
    * @param authentication
    * @param groupMsgContent
    */
   @MessageMapping("/ws/groupChat")
   public void handleGroupMessage(Authentication authentication, GroupMsgContent groupMsgContent) {
      //从Security中获取当前登录的用户信息
      User user = (User) authentication.getPrincipal();
      log.debug("当前登录的用户信息：{}", user);
      log.debug("接受到前端传来的群聊消息：{}", groupMsgContent);
      //设置Emoji内容,转换成unicode编码
//      groupMsgContent.setContent(emojiConverter.toHtml(groupMsgContent.getContent()));
      //设置转发的消息信息
      groupMsgContent.setFromId(user.getId());
      groupMsgContent.setFromName(user.getNickname());
      groupMsgContent.setFromProfile(user.getUserProfile());
      groupMsgContent.setCreateTime(new Date());
      //保存群聊消息到数据库中
      log.debug("即将保存的群聊消息：{}", groupMsgContent);
      groupMsgContentService.insert(groupMsgContent);
      //转发数据
      simpMessagingTemplate.convertAndSend("/topic/greetings", groupMsgContent);
   }

   /**
    * 单聊消息转发
    *
    * @param authentication
    * @param message
    */
   @MessageMapping("/ws/chat")
   public void handleMessage(Authentication authentication, Message message) {
      User user = (User) authentication.getPrincipal();
      log.debug("当前登录的用户信息：{}", user);
      log.debug("接受到前端传来的单聊消息：{}", message);
      //设置转发的消息信息
      message.setFrom(user.getUsername());
      message.setCreateTime(new Date());
      message.setFromNickname(user.getNickname());
      simpMessagingTemplate.convertAndSendToUser(message.getTo(), "/queue/chat", message);
   }
}
