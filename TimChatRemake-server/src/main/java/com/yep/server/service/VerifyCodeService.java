package com.yep.server.service;

/**
 * @author HuangSir
 * @date 2022-02-19 21:49
 */
public interface VerifyCodeService {
   String getCode();

   void sendVerifyCodeEmail();

}
