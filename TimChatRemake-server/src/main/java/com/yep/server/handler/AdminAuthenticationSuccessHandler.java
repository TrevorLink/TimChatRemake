package com.yep.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yep.server.pojo.Admin;
import com.yep.server.pojo.RespBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义管理员登陆成功处理器
 * @author HuangSir
 * @date 2022-02-20 9:51
 */
@Component
public class AdminAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
   @Override
   public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
      httpServletResponse.setContentType("application/json;charset=utf-8");
      PrintWriter out = httpServletResponse.getWriter();
      Admin admin = (Admin) authentication.getPrincipal();
      //登录成功后把管理员密码不要给前端
      admin.setPassword(null);
      RespBean ok = RespBean.ok("登录成功", admin);
      String s = new ObjectMapper().writeValueAsString(ok);
      out.write(s);
      out.flush();
      out.close();
   }
}
