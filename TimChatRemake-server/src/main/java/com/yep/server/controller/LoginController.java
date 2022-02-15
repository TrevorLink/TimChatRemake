package com.yep.server.controller;

import com.yep.server.util.VerificationCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 用户登录controller
 * @author HuangSir
 * @date 2022-02-15 10:27
 */
@Controller
@Slf4j
public class LoginController {
   /**
    * 获取登录的验证码图片
    * @param response
    * @param session
    * @throws IOException
    */
   @GetMapping("/verifyCode")
   public void getVerifyCode(HttpServletResponse response, HttpSession session) throws IOException {
      VerificationCode code = new VerificationCode();
      //根据工具类生成验证码
      BufferedImage image = code.getImage();
      String text = code.getText();
      log.debug("生成的验证码为：{}",text);
      //将验证码的答案保存到Session中，如果有就覆盖
      session.setAttribute("verify_code",text);
      VerificationCode.output(image,response.getOutputStream());
   }
}
