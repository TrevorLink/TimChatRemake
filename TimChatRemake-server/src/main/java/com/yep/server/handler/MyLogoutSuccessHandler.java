package com.yep.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yep.server.pojo.RespBean;
import com.yep.server.pojo.User;
import com.yep.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义登出成功处理器
 * @author HuangSir
 * @date 2022-02-15 0:10
 */
@Component
@Slf4j
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
   @Autowired
   private UserService userService;
   @Autowired
   private SimpMessagingTemplate simpleMessagingTemplate;
   @Override
   public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
      User user = (User) authentication.getPrincipal();
      log.debug("当前登录的用户信息：{}",user);
      //用户登出时设置用户的状态为离线
      userService.setUserStateToLeave(user.getId());
      //系统广播
      log.debug("系统消息：用户【"+user.getNickname()+"】润了");
      simpleMessagingTemplate.convertAndSend("/topic/notification","系统消息：用户【"+user.getNickname()+"】润了");
      response.setContentType("application/json;charset=utf-8");
      PrintWriter out=response.getWriter();
      out.write(new ObjectMapper().writeValueAsString(RespBean.ok("注销成功！")));
      out.flush();
      out.close();
   }
}
