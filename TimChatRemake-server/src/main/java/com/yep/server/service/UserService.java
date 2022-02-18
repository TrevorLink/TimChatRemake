package com.yep.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yep.server.pojo.RespPageBean;
import com.yep.server.pojo.User;

/**
 * @author HuangSir
 * @date 2022-02-14 23:48
 */
public interface UserService  extends IService<User> {
   void setUserStateToOn(Integer id);

   void setUserStateToLeave(Integer id);

   RespPageBean getAllUserByPage(Integer page, Integer size, String keyword, Integer isLocked);

   int changeLockedStatus(Integer id, Boolean isLocked);
}
