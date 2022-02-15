package com.yep.server.service.impl;

import com.yep.server.mapper.UserMapper;
import com.yep.server.pojo.User;
import com.yep.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * @author HuangSir
 * @date 2022-02-14 23:48
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService,UserDetailsService {
   @Autowired(required = true)
   private UserMapper userMapper;
   @Override
   public User loadUserByUsername(String username) {
      User user = userMapper.loadUserByUsername(username);
      if (user==null) throw  new UsernameNotFoundException("用户不存在！");
      log.debug("查到的用户数据为：{}",user);
      return  user;
   }

   @Override
   public void setUserStateToOn(Integer id) {
      userMapper.setUserStateToOn(id);
   }

   @Override
   public void setUserStateToLeave(Integer id) {
      userMapper.setUserStateToLeave(id);
   }
}
