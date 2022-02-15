package com.yep.server.config;

import com.yep.server.pojo.User;
import com.yep.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * @author HuangSir
 * @date 2022-02-14 23:49
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
   @Autowired
   private UserDetailsService userDetailsService;
   @Autowired
   private AuthenticationSuccessHandler successHandler;
   @Autowired
   private AuthenticationFailureHandler failureHandler;
   @Autowired
   private LogoutSuccessHandler logoutSuccessHandler;

   //调用这个configure方法实现从数据库中获取用户信息
   @Override
   protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(userDetailsService);
   }
   //密码加密
   @Bean
   PasswordEncoder passwordEncoder(){
      return new BCryptPasswordEncoder();
   }

   //总体配置
   @Override
   protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests()
              .anyRequest()
              .authenticated()
              .and()
              .formLogin()
              .usernameParameter("username")
              .passwordParameter("password")
              .loginPage("/login")
              .loginProcessingUrl("/doLogin")
              .successHandler(successHandler)
              .failureHandler(failureHandler)
              .permitAll()//返回值直接返回
              .and()
              .logout()
              .logoutUrl("/logout")
              .logoutSuccessHandler(logoutSuccessHandler)
              .permitAll()
              .and()
              .csrf().disable().exceptionHandling();
   }
}
