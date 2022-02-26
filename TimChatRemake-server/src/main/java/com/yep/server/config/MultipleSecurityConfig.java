package com.yep.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yep.server.filter.VerificationCodeFilter;
import com.yep.server.handler.*;
import com.yep.server.pojo.Admin;
import com.yep.server.pojo.RespBean;
import com.yep.server.service.UserService;
import com.yep.server.service.impl.AdminServiceImpl;
import com.yep.server.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author HuangSir
 * @date 2022-02-19 22:07
 */
@EnableWebSecurity
public class MultipleSecurityConfig {
   //虽然有两种不同的用户认证策略但是还是少不了密码加密捏
   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }
   @Configuration
   @Order(1)
   public static class AdminSecurityConfig extends WebSecurityConfigurerAdapter{
      @Autowired
      AdminServiceImpl adminService;
      @Autowired
      VerificationCodeFilter verificationCodeFilter;
      @Autowired
      MyAuthenticationFailureHandler myAuthenticationFailureHandler;
      @Autowired
      AdminAuthenticationSuccessHandler adminAuthenticationSuccessHandler;
      @Autowired
      AdminLogoutSuccessHandler adminLogoutSuccessHandler;
      @Autowired
      public   AuthenticationEntryPoint authenticationEntryPoint;

      //管理员登录的用户名和密码验证服务
      @Override
      protected void configure(AuthenticationManagerBuilder auth) throws Exception {
         auth.userDetailsService(adminService);
      }

      //忽略"/login","/verifyCode"请求，该请求不需要进入Security的拦截器
      @Override
      public void configure(WebSecurity web) throws Exception {
         web.ignoring().antMatchers("/css/**","/fonts/**","/img/**","/js/**","/favicon.ico","/index.html","/admin/login","/admin/mailVerifyCode");
      }
      //http请求验证和处理规则，响应处理的配置
      @Override
      protected void configure(HttpSecurity http) throws Exception {
         //将验证码过滤器添加在用户名密码过滤器的前面
         http.addFilterBefore(verificationCodeFilter, UsernamePasswordAuthenticationFilter.class);
         http.antMatcher("/admin/**").authorizeRequests()
                 .anyRequest().authenticated()
                 .and()
                 .formLogin()
                 .usernameParameter("username")
                 .passwordParameter("password")
                 .loginPage("/admin/login")
                 .loginProcessingUrl("/admin/doLogin")
                 //成功处理
                 .successHandler(adminAuthenticationSuccessHandler)
                 //失败处理
                 .failureHandler(myAuthenticationFailureHandler)
                 .permitAll()//返回值直接返回
                 .and()
                 //登出处理
                 .logout()
                 .logoutUrl("/admin/logout")
                 .logoutSuccessHandler(adminLogoutSuccessHandler)
                 .permitAll()
                 .and()
                 .csrf().disable()//关闭csrf防御方便调试
                 //没有认证时，在这里处理结果，不进行重定向到login页
                 .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
      }
   }
   @Configuration
   @Order(2)
   public static class UserSecurityConfig extends WebSecurityConfigurerAdapter {
      @Autowired
      private UserServiceImpl userService;
      @Autowired
      private  VerificationCodeFilter verificationCodeFilter;
      @Autowired
      private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
      @Autowired
      private MyAuthenticationFailureHandler myAuthenticationFailureHandler;
      @Autowired
      private  MyLogoutSuccessHandler myLogoutSuccessHandler;
      @Autowired
      public   AuthenticationEntryPoint authenticationEntryPoint;
      //验证服务，设置从数据库中读取普通用户信息
      @Override
      protected void configure(AuthenticationManagerBuilder auth) throws Exception {
         auth.userDetailsService(userService);
      }


         //忽略"/login","/verifyCode"请求，该请求不需要进入Security的拦截器
         @Override
         public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/login","/verifyCode","/file","/user/register","/user/checkUsername","/user/checkNickname");
         }
      //登录验证
      @Override
      protected void configure(HttpSecurity http) throws Exception {
         //将验证码过滤器添加在用户名密码过滤器的前面
         http.addFilterBefore(verificationCodeFilter, UsernamePasswordAuthenticationFilter.class);
         http.authorizeRequests()
                 .anyRequest().authenticated()
                 .and()
                 .formLogin()
                 .usernameParameter("username")
                 .passwordParameter("password")
                 .loginPage("/login")
                 .loginProcessingUrl("/doLogin")
                 //成功处理
                 .successHandler(myAuthenticationSuccessHandler)
                 //失败处理
                 .failureHandler(myAuthenticationFailureHandler)
                 .permitAll()//返回值直接返回
                 .and()
                 //登出处理
                 .logout()
                 .logoutUrl("/logout")
                 .logoutSuccessHandler(myLogoutSuccessHandler)
                 .permitAll()
                 .and()
                 .csrf().disable()//关闭csrf防御方便调试
                 //没有认证时，在这里处理结果，不进行重定向到login页
                 .exceptionHandling().authenticationEntryPoint( authenticationEntryPoint);
      }
   }
}
