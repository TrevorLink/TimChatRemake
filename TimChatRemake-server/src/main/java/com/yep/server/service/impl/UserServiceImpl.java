package com.yep.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yep.server.mapper.UserMapper;
import com.yep.server.pojo.RespPageBean;
import com.yep.server.pojo.User;
import com.yep.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author HuangSir
 * @date 2022-02-14 23:48
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService,UserDetailsService  {
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

   @Override
   public RespPageBean getAllUserByPage(Integer page, Integer size, String keyword, Integer isLocked) {
      if(page!=null && size!=null) page=(page-1)*size;//将前端返回的页数转化成数据库中的起始行数
      //获取分页数据
      List<User> userList = userMapper.getAllUserByPage(page,size,keyword,isLocked);
      //获取用户数据的总行数
      Long total = userMapper.getTotal(keyword,isLocked);
      return new RespPageBean(total,userList);
   }

   @Override
   public int changeLockedStatus(Integer id, Boolean isLocked) {
      return  userMapper.changeLockedStatus(id,isLocked);
   }

   @Override
   public List<User> getAllUsersWithoutCurrentUser() {
      //获取到当前登录的用户对象
      User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      Integer id = user.getId();
      QueryWrapper<User> wrapper = new QueryWrapper<>();
      wrapper.ne("id",id);
      return userMapper.selectList(wrapper);
   }
}
