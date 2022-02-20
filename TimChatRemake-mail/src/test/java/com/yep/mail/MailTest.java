package com.yep.mail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Date;

/**
 * @author HuangSir
 * @date 2022-02-19 21:33
 */
@SpringBootTest
public class MailTest {

   @Autowired
   public JavaMailSender javaMailSender;
   @Value("${spring.mail.username}")
   private  String from;
   //测试邮件发送（只能在本地跑，服务器好像八行）
   @Test
   public  void testMailSend(){
      SimpleMailMessage msg = new SimpleMailMessage();
      //邮件的主题
      msg.setSubject("这是测试邮件主题");
      //邮件的内容
      msg.setText("这是测试邮件内容:\nsecond try");
      //邮件的发送方，对应配置文件中的spring.mail.username
      msg.setFrom(from);
      //邮件发送时间
      msg.setSentDate(new Date());
      //邮件接收方
      msg.setTo("2845964844@qq.com");
      javaMailSender.send(msg);
   }
}
