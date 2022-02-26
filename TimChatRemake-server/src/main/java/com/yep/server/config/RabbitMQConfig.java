package com.yep.server.config;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yep.server.pojo.MailConstants;
import com.yep.server.pojo.MailSendLog;
import com.yep.server.service.MailSendLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 * @author HuangSir
 * @date 2022-02-21 10:44
 */
@Configuration
@Slf4j
public class RabbitMQConfig {
   @Autowired
   private CachingConnectionFactory cachingConnectionFactory;
   @Autowired
   private MailSendLogService mailSendLogService;
   @Bean
   public RabbitTemplate rabbitTemplate(){
      RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
      //设置rabbitTemplate中消息从生产者到达exchange的回调函数
      rabbitTemplate.setConfirmCallback((data,ack,cause)->{
         String msgId = data.getId();
         if(ack){
            log.info(msgId+"消息发送到交换机成功！");
            //修改数据库中的邮件日志记录，消息发送成功就设置status为1
            MailSendLog log = new MailSendLog();
            log.setMsgId(msgId);
            log.setStatus(MailConstants.SUCCESS);
            mailSendLogService.updateById(log);
         }else{
            log.error("消息投递到交换机失败！");
         }
      });
      rabbitTemplate.setReturnCallback((msg,repCode,repText,exchange,routingKey)->{
         log.error("{}---消息从交换机投递到队列中失败！具体原因：{}",msg.getBody(),repText);
         log.error("发生错误的交换机：{}发生错误的路由key：{}",exchange,routingKey);
      });
      return rabbitTemplate;
   }
}
