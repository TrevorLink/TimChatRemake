package com.yep.mail.receiver;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * 验证码消费者
 * @author HuangSir
 * @date 2022-02-22 9:50
 */
@Component
@Slf4j
public class VerifyCodeReceiver {

   @Autowired
   private RedisTemplate redisTemplate;
   @Autowired
   private JavaMailSender javaMailSender;

   @RabbitListener(queues = "${mail.queue.verifyCode:mail-queue-verifyCode}")
   public void receiveVerifyCode(Message message, Channel channel) throws IOException {
      //获得队列中的消息，其实就是验证码
      String code = message.getPayload().toString();
      //获取消息头中的标签信息
      MessageHeaders headers = message.getHeaders();
      Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
      //获取消息ID
      String msgId = headers.get("spring_returned_message_correlation").toString();
      log.info("正在处理的消息【{}】",msgId);
      //查看redis中是否存在当前消息
      if(redisTemplate.opsForHash().entries("mail_log").containsKey(msgId)){
         //手动确认消息被消费
         channel.basicAck(tag,true);
         log.info("消息【{}】被重复消费",msgId);
         return;
      }
      SimpleMailMessage msg = new SimpleMailMessage();
      msg.setSubject("管理员端登录验证码");
      msg.setText("本次登录的验证码："+code);
      msg.setFrom("2845964844@qq.com");
      msg.setSentDate(new Date());
      msg.setTo("2845964844@qq.com");
      javaMailSender.send(msg);

   }
}
