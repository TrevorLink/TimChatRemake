package com.yep.server.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yep.server.pojo.RespBean;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 验证码校验的额外过滤器
 *
 * @author HuangSir
 * @date 2022-02-15 10:22
 */
@Component
public class VerificationCodeFilter extends GenericFilter {
   @Override
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
      HttpServletRequest httpServletRequest = (HttpServletRequest) request;
      HttpServletResponse httpServletResponse = (HttpServletResponse) response;
      //如果是登录的表单我们才拦截，否则放行
      if ("POST".equals(httpServletRequest.getMethod()) && "/doLogin".equals(httpServletRequest.getServletPath())) {
         //获取请求参数中的验证码（用户传来的）
         String code = httpServletRequest.getParameter("code");
         //从Session中获取之前登录的时候存储的验证码正确答案
         String verify_code = (String) httpServletRequest.getSession().getAttribute("verify_code");
         httpServletResponse.setContentType("application/json;charset=utf-8");
         PrintWriter writer = httpServletResponse.getWriter();
         try {
            //验证是否相同,如果有错误就向客户端返回错误的JSON
            if (!code.equalsIgnoreCase(verify_code)) {
               //输出json
               writer.write(new ObjectMapper().writeValueAsString(RespBean.error("验证码错误！")));
            } else {
               //匹配成功才放行
               chain.doFilter(request, response);
            }
         } catch (NullPointerException e) {
            writer.write(new ObjectMapper().writeValueAsString(RespBean.error("请求异常，请重新请求！")));
         } finally {
            writer.flush();
            writer.close();
         }
//      } else if ("POST".equals(httpServletRequest.getMethod())&&"/admin/doLogin".equals(httpServletRequest.getServletPath())){
//         //获取输入的验证码
//         String mailCode = request.getParameter("mailCode");
//         //获取session中保存的验证码
//         String verifyCode = ((String) httpServletRequest.getSession().getAttribute("admin_verifyCode"));
//         //构建响应输出流
//         response.setContentType("application/json;charset=utf-8");
//         PrintWriter printWriter =response.getWriter();
//         try {
//            if(!mailCode.equals(verifyCode)){
//               printWriter.write(new ObjectMapper().writeValueAsString(RespBean.error("验证码错误！")));
//            }else {
//               chain.doFilter(request,response);
//            }
//         }catch (NullPointerException e){
//            printWriter.write(new ObjectMapper().writeValueAsString(RespBean.error("请求异常，请重新请求！")));
//         }finally {
//            printWriter.flush();
//            printWriter.close();
//         }
//      }
      }else {
         chain.doFilter(request,response);
      }
   }
}
