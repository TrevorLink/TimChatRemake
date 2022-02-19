package com.yep.server.controller;

import com.yep.server.pojo.User;
import com.yep.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 单聊信息接口
 *
 * @author HuangSir
 * @date 2022-02-19 14:42
 */
@RestController
@RequestMapping("/chat")
public class ChatController {
   @Autowired
   private UserService userService;

   @GetMapping("/users")
   public List<User> getAllUsersWithoutCurrentUser() {
      return userService.getAllUsersWithoutCurrentUser();
   }
}
