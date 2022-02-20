package com.yep.server.service.impl;

import com.yep.server.service.VerifyCodeService;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author HuangSir
 * @date 2022-02-19 21:49
 */
@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {
   /**
    * 管理端登录随机生成四位验证码
    * @return
    */
   @Override
   public String getCode() {
      //获取随机的四个数字
      StringBuilder code = new StringBuilder();
      for (int i = 0; i < 4; i++) {
         code.append(new Random().nextInt(10));
      }
      return code.toString();
   }

   @Override
   public void sendVerifyCodeEmail() {

   }

}
