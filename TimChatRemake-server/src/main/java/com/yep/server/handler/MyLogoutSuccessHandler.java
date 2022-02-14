package com.yep.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yep.server.pojo.RespBean;
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
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
   @Override
   public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
      response.setContentType("application/json;charset=utf-8");
      PrintWriter out=response.getWriter();
      out.write(new ObjectMapper().writeValueAsString(RespBean.ok("注销成功！")));
      out.flush();
      out.close();
   }
}
