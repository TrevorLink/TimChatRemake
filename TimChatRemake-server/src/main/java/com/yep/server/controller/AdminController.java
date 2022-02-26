package com.yep.server.controller;

import com.yep.server.pojo.Admin;
import com.yep.server.pojo.RespBean;
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
   public RespBean selectOne(Integer id){
      Admin admin = adminService.getById(id);
      if (admin==null) {
         return RespBean.error("该管理员不存在！");
      }
      return RespBean.ok("",admin);
   }
}
