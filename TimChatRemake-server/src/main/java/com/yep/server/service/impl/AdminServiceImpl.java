package com.yep.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yep.server.mapper.AdminMapper;
import com.yep.server.pojo.Admin;
import com.yep.server.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author HuangSir
 * @date 2022-02-20 9:28
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>implements AdminService, UserDetailsService {
   @Autowired
   private AdminMapper adminMapper;

   /**
    * 使用Security从数据库中获取Admin对象
    * @param username
    * @return
    * @throws UsernameNotFoundException
    */
   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      QueryWrapper<Admin> wrapper = new QueryWrapper<>();
      wrapper.eq("username",username);
      Admin admin = adminMapper.selectOne(wrapper);
      if(admin==null) throw  new UsernameNotFoundException("该管理员不存在！");
      return  admin;
   }

}
