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
 * 管理员登出成功处理器
 * @author HuangSir
 * @date 2022-02-22 14:23
 */
@Component
public class AdminLogoutSuccessHandler implements LogoutSuccessHandler {
   @Override
   public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
      httpServletResponse.setContentType("application/json;charset=utf-8");
      PrintWriter out=httpServletResponse.getWriter();
      out.write(new ObjectMapper().writeValueAsString(RespBean.ok("管理员退出成功！")));
      out.flush();
      out.close();
   }
}
