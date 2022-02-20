package com.yep.server.controller;

import com.yep.server.pojo.Admin;
import com.yep.server.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员控制器
 */
@RestController
@RequestMapping("admin")
public class AdminController {
   @Autowired
   private AdminService adminService;

   @RequestMapping("selectOne")
   public Admin selectOne(Integer id){
      return adminService.getById(id);
   }
}
