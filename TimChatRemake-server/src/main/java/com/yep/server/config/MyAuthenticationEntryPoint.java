package com.yep.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yep.server.pojo.RespBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 未登陆的处理
 * @author HuangSir
 * @date 2022-02-22 14:52
 */
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
   @Override
   public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
      httpServletResponse.setContentType("application/json;charset=utf-8");
      PrintWriter out=httpServletResponse.getWriter();
      out.write(new ObjectMapper().writeValueAsString(RespBean.error("请先登录捏")));
      out.flush();
      out.close();
   }
}
