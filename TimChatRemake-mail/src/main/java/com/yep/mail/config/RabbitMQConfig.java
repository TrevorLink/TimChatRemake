package com.yep.mail.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HuangSir
 * @date 2022-02-22 8:43
 */
@Configuration
public class RabbitMQConfig {
   @Value("${mail.exchange:mail-exchange}")
   private String mailExchange;

   @Value("${mail.queue.verifyCode:mail-queue-verifyCode}")
   private String mailQueueVerifyCode;

   @Value("${mail.route.verifyCode:mail-route-verifyCode}")
   private String mailRouteVerifyCode;

   @Value("${mail.queue.feedback:mail-queue-feedback}")
   private String mailQueueFeedback;

   @Value("${mail.route.feedback:mail-route-feedback}")
   private String mailRouteFeedback;


   @Bean
   DirectExchange mailExchange(){
      return new DirectExchange(mailExchange,true,false);
   }

   /**
    * 验证码消息队列
    * @return
    */
   @Bean
   Queue mailQueueVerifyCode(){
      //定义持久化队列
      return new Queue(mailQueueVerifyCode,true);
   }

   /**
    * 验证码队列绑定交换机并指定路由key
    * @return
    */
   @Bean
   Binding mailQueueVerifyCodeBinding(){
      return BindingBuilder.bind(mailQueueVerifyCode()).to(mailExchange()).with(mailRouteVerifyCode);
   }
   @Bean
   Queue mailQueueFeedback(){
      //定义持久化队列
      return new Queue(mailQueueFeedback,true);
   }
   /**
   反馈队列和交换机绑定并指定路由key
    */
   @Bean
   Binding mailQueueFeedbackBinding(){
      return BindingBuilder.bind(mailQueueFeedback()).to(mailExchange()).with(mailRouteFeedback);
   }
}
