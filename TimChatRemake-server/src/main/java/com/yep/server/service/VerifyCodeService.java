package com.yep.server.service;

/**
 * @author HuangSir
 * @date 2022-02-19 21:49
 */
public interface VerifyCodeService {
   /**
    * 生成随机验证码
    * @return
    */
   String getCode();

   /**
    * 管理员登录发送邮箱验证码
    * @param code
    */
   void sendVerifyCodeEmail(String code);

}
