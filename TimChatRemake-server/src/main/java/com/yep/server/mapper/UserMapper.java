package com.yep.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yep.server.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author HuangSir
 * @date 2022-02-14 23:38
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
   /**
    * 根据用户名查询用户对象，这个是security要求实现的方法
    * @param username
    * @return
    */
   User loadUserByUsername(String username);

   void setUserStateToOn(Integer id);

   void setUserStateToLeave(Integer id);

   List<User> getAllUserByPage(Integer page, Integer size, String keyword, Integer isLocked);

   Long getTotal(String keyword, Integer isLocked);

   int changeLockedStatus(Integer id, Boolean isLocked);
}
