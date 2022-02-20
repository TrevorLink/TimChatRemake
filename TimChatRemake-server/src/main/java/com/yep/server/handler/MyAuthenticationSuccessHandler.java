package com.yep.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yep.server.pojo.RespBean;
import com.yep.server.pojo.User;
import com.yep.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义普通用户认证成功的处理器
 * @author HuangSir
 * @date 2022-02-15 0:03
 */
@Component
@Slf4j
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
   @Autowired
   private  UserService userService;
   @Autowired
   private SimpMessagingTemplate simpMessagingTemplate;
   //登录成功返回公共JSON
   @Override
   public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
      response.setContentType("application/json;charset=utf-8");
      PrintWriter out=response.getWriter();
      User user=(User) authentication.getPrincipal();
      log.debug("当前登录的用户信息：{}",user);
      //密码不要给前端
      user.setPassword(null);
      //登录成功后在聊天室里更新用户的状态为在线
      userService.setUserStateToOn(user.getId());
      user.setUserStateId(1);
      //广播系统通知消息
      log.debug("系统消息：欢迎用户【"+user.getNickname()+"】进入聊天室");
      simpMessagingTemplate.convertAndSend("/topic/notification","系统消息：欢迎用户【"+user.getNickname()+"】进入聊天室");
      RespBean respBean = RespBean.ok("登录成功", user);
      String s = new ObjectMapper().writeValueAsString(respBean);
      out.write(s);
      out.flush();
      out.close();
   }
}
