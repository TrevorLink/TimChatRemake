package com.yep.server.mapper;

import com.yep.server.pojo.User;
import org.springframework.stereotype.Repository;

/**
 * @author HuangSir
 * @date 2022-02-14 23:38
 */
@Repository
public interface UserMapper {
   /**
    * 根据用户名查询用户对象，这个是security要求实现的方法
    * @param username
    * @return
    */
   User loadUserByUsername(String username);

   void setUserStateToOn(Integer id);

   void setUserStateToLeave(Integer id);
}
