package com.yep.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

/**
 * 管理员实体类
 * @author HuangSir
 * @date 2022-02-20 9:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin implements Serializable, UserDetails {
   private static final long serialVersionUID = -75235725571250857L;

   private Integer id;
   /**
    * 登录账号
    */
   private String username;
   /**
    * 昵称
    */
   private String nickname;
   /**
    * 密码
    */
   private String password;
   /**
    * 管理员头像
    */
   private String userProfile;

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return null;
   }

   @Override
   public String getPassword() {
      return password;
   }

   @Override
   public String getUsername() {
      return username;
   }

   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return true;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return true;
   }
}
