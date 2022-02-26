package com.yep.server.service.impl;

import com.yep.server.pojo.MailConstants;
import com.yep.server.pojo.MailSendLog;
import com.yep.server.service.MailSendLogService;
import com.yep.server.service.VerifyCodeService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @author HuangSir
 * @date 2022-02-19 21:49
 */
@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {
   @Autowired
   RabbitTemplate rabbitTemplate;

   @Autowired
   MailSendLogService mailSendLogService;

   @Value("${mail.exchange:mail-exchange}")
   private String mailExchange;

   @Value("${mail.route.verifyCode:mail-route-verifyCode}")
   private String mailRouteVerifyCode;

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
   public void sendVerifyCodeEmail(String code) {
      //①仙布着急去把验证码作为内容直接发送到消息队列中，先添加消息发送日志记录到数据库中
      String msgId = UUID.randomUUID().toString();
      MailSendLog mailSendLog = new MailSendLog();
      mailSendLog.setMsgId(msgId);
      mailSendLog.setContent(code);
      mailSendLog.setContentType(MailConstants.VERIFY_CODE_TYPE);
      mailSendLog.setCount(1);
      mailSendLog.setCreateTime(new Date());
      mailSendLog.setTryTime(new Date(System.currentTimeMillis()+1000*10*MailConstants.MEG_TIMEOUT));
      mailSendLog.setUpdateTime(new Date());
      mailSendLog.setExchange(mailExchange);
      mailSendLog.setRouteKey(mailRouteVerifyCode);
      mailSendLog.setStatus(MailConstants.DELIVERING);
      mailSendLogService.save(mailSendLog);
      //②生产者发送消息到消息队列，内容就是传入的验证码，前面这么做都是为了确保消息可靠性插入数据库的日志记录
      rabbitTemplate.convertAndSend(mailExchange,mailRouteVerifyCode,code,new CorrelationData(msgId));
   }

}
