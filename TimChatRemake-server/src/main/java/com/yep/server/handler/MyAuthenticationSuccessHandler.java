package com.yep.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yep.server.pojo.RespBean;
import com.yep.server.pojo.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义认证成功的处理器
 * @author HuangSir
 * @date 2022-02-15 0:03
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
   //登录成功返回公共JSON
   @Override
   public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
      response.setContentType("application/json;charset=utf-8");
      PrintWriter out=response.getWriter();
      User user=(User) authentication.getPrincipal();
      //密码不要给前端
      user.setPassword(null);
      RespBean respBean = RespBean.ok("登录成功", user);
      String s = new ObjectMapper().writeValueAsString(respBean);
      out.write(s);
      out.flush();
      out.close();
   }
}
