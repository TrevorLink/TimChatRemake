package com.yep.mail.receiver;

import com.rabbitmq.client.Channel;
import com.rabbitmq.tools.json.JSONUtil;
import com.yep.mail.pojo.Feedback;
import com.yep.mail.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * 反馈消息的消费者
 * @author HuangSir
 * @date 2022-02-22 9:29
 */
@Component
@Slf4j
public class FeedbackReceiver {
   @Autowired
   private JavaMailSender javaMailSender;
   @Autowired
   private RedisTemplate redisTemplate;
   /**
    * 监听反馈消息的消息队列
    */
   @RabbitListener(queues ="${mail.queue.feedback:mail-queue-feedback}")
   public void getFeedbackMessage(Message message, Channel channel) throws IOException {
      //获取到消息队列中的消息内容
      String s = message.getPayload().toString();
      MessageHeaders headers = message.getHeaders();
      Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
      String msgId = headers.get("spring_returned_message_correlation").toString();
      log.info("【{}】---正在处理的消息",msgId);
      //如果此条消息已经在Redis中存在
      if(redisTemplate.opsForHash().entries("mail_log").containsKey(msgId)){
         //消费消息
         channel.basicAck(tag,true);
         log.info("消息id{}存在重复消费",msgId);
         return;
      }
      try {
         //将消息内容反序列化为Feedback实体
         Feedback feedback = JsonUtil.parseToObject(s, Feedback.class);
         log.info("即将发送的反馈消息内容：{}",feedback.getContent());
         SimpleMailMessage mailMessage = new SimpleMailMessage();
         mailMessage.setSubject("来自用户的意见反馈");
         //拼接邮件信息
         StringBuilder formatMessage = new StringBuilder();
         formatMessage.append("用户编号："+feedback.getUserId()+"\n");
         formatMessage.append("用户名："+feedback.getUsername()+"\n");
         formatMessage.append("用户昵称："+feedback.getNickname()+"\n");
         formatMessage.append("反馈内容："+feedback.getContent());
         log.info("即将发送的邮件信息：{}",formatMessage);
         //设置邮件消息
         mailMessage.setText(formatMessage.toString());
         mailMessage.setFrom("2845964844@qq.com");
         mailMessage.setTo("2845964844@qq.com");
         mailMessage.setSentDate(new Date());
         javaMailSender.send(mailMessage);
         //邮件发送完毕更新Redis的Hash
         redisTemplate.opsForHash().put("mail_log",msgId,feedback.getContent());
         //手动确认消息处理完毕
         channel.basicAck(tag,true);
      }catch (IOException e) {
         //发生异常就手动确认消息消费失败，返回队列中
         channel.basicNack(tag,false,true);
         log.info("消息【{}】重新返回消息队列中",msgId);
         e.printStackTrace();
      }
   }
}
