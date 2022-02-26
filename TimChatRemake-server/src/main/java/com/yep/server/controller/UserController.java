package com.yep.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yep.server.pojo.RespBean;
import com.yep.server.pojo.RespPageBean;
import com.yep.server.pojo.User;
import com.yep.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 用户管理接口
 * @author HuangSir
 * @date 2022-02-17 16:20
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
   @Autowired
   private UserService userService;

   /**
    * 用户注册
    *
    * @param user
    * @return
    */
   @PostMapping("/register")
   public RespBean addUser(@RequestBody User user) {
      log.debug("获取到的用户对象：{}",user);
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      //对用户密码进行加密后存入数据库
      user.setPassword(encoder.encode(user.getPassword()));
      user.setUserStateId(2);
      user.setEnabled(true);
      user.setLocked(false);
      if (userService.save(user)) {
         return RespBean.ok("注册成功！");
      } else {
         return RespBean.error("注册失败！");
      }
   }

   /**
    * 检查用户名是否可用
    *
    * @param username
    * @return
    */
   @GetMapping("/checkUsername")
   public Integer checkUsername(@RequestParam("username") String username) {
      QueryWrapper wrapper = new QueryWrapper();
      wrapper.eq("username", username);
      long count = userService.count(wrapper);
      return Math.toIntExact(count);
   }

   /**
    * 检查昵称是否可用
    *
    * @param nickname
    * @return
    */
   @GetMapping("/checkNickname")
   public Integer checkNickname(@RequestParam("nickname") String nickname) {
      QueryWrapper wrapper = new QueryWrapper();
      wrapper.eq("nickname", nickname);
      long count = userService.count(wrapper);
      return Math.toIntExact(count);
   }

   /**
    * 查询单条用户信息
    *
    * @param id
    * @return
    */
   @GetMapping("selectOne")
   public RespBean selectOne(Integer id) {
      QueryWrapper wrapper = new QueryWrapper();
      wrapper.eq("id", id);
      User user = userService.getOne(wrapper);
      if(user==null){
         return RespBean.error("该用户不存在！");
      }
      return RespBean.ok("",user);
   }

   /**
    * 管理员端查询用户的分页信息
    *
    * @param page     当前页数
    * @param size     每页查询的记录
    * @param keyword  关键词，用于搜索筛选
    * @param isLocked 是否锁定，用于搜索筛选
    * @return
    */
   @GetMapping("/")
   public RespPageBean getAllUserByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                        @RequestParam(value = "size", defaultValue = "10") Integer size,
                                        String keyword, Integer isLocked) {
      log.debug("page:{}",page);
      log.debug("size:{}",size);
      Page<User> pageModel = new Page<>(page, size);
      QueryWrapper<User> wrapper = new QueryWrapper<>();
      if (isLocked != null) {
         wrapper.eq("is_locked", isLocked);
      }
      if (keyword != null) {
         wrapper.like("nickname", keyword);
      }
      Page<User> pageRes = userService.page(pageModel, wrapper);
      List<User> users = pageRes.getRecords();
      long count = userService.count(wrapper);
      return new RespPageBean(count, users);
   }

   /**
    * 根据指定id修改用户状态信息
    *
    * @param id
    * @param isLocked
    * @return
    */
   @PutMapping("/")
   public RespBean changeLockedStatus(@RequestParam("id") Integer id, Boolean isLocked) {
      log.debug("即将修改的用户id:{}",id);
      log.debug("即将修改的isLocked:{}",isLocked);
      User user = new User();
      user.setId(id);
      user.setLocked(isLocked);
      user.setEnabled(true);
      log.debug("即将修改后的用户信息：{}",user);
      boolean update = userService.updateById(user);
      if (update) return RespBean.ok("修改用户状态成功！");
      else return RespBean.error("修改用户状态失败！");
   }

   /**
    * 根据ID删除用户信息
    *
    * @param id
    * @return
    */
   @DeleteMapping("/{id}")
   public RespBean deleteUser(@PathVariable Integer id) {
      boolean remove = userService.removeById(id);
      if (remove) return RespBean.ok("删除用户成功！");
      else return RespBean.error("删除用户失败！");
   }

   /**
    * 批量删除用户信息
    *
    * @param ids
    * @return
    */
   @DeleteMapping("/")
   public RespBean deleteUsers(Integer[] ids) {
      log.debug("要删除的用户ID们：{}",ids);
      boolean remove = userService.removeByIds(Arrays.asList(ids));
      if (remove) return RespBean.ok("批量删除用户成功！");
      else return RespBean.error("批量删除用户失败！");
   }
}
