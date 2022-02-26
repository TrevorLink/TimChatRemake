package com.yep.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yep.server.mapper.FeedbackMapper;
import com.yep.server.pojo.Feedback;
import com.yep.server.pojo.MailConstants;
import com.yep.server.pojo.MailSendLog;
import com.yep.server.service.FeedbackService;
import com.yep.server.service.MailSendLogService;
import com.yep.server.utils.JsonUtil;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * @author HuangSir
 * @date 2022-02-21 12:04
 */
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {
   @Autowired
   private FeedbackMapper feedbackMapper;

   @Autowired
   RabbitTemplate rabbitTemplate;

   @Value("${mail.exchange:mail-exchange}")
   private String mailExchange;

   @Value("${mail.queue.feedback:mail-queue-feedback}")
   private String mailQueueFeedback;

   @Value("${mail.route.feedback:mail-route-feedback}")
   private String mailRouteFeedback;

   @Autowired
   private MailSendLogService mailSendLogService;

   @Override
   public void sendFeedbackToMail(Feedback feedback) {
      //①设置反馈的ID编号为随机的UUID
      feedback.setId(UUID.randomUUID().toString());
          //向反馈表中存储数据
      feedbackMapper.insert(feedback);
      String json = JsonUtil.parseToString(feedback);
      //②添加消息发送日志的数据库记录
      String msgId=UUID.randomUUID().toString();
      MailSendLog sendLog = new MailSendLog();
      sendLog.setMsgId(msgId);
      sendLog.setContent(json);
      sendLog.setContentType(MailConstants.FEEDBACK_TYPE);
      sendLog.setCount(1);
      sendLog.setCreateTime(new Date());
      sendLog.setUpdateTime(new Date());
         //设置超过一分钟后开始重试
      sendLog.setTryTime(new Date(System.currentTimeMillis()+1000*60*MailConstants.MEG_TIMEOUT));
      sendLog.setExchange(mailExchange);
      sendLog.setRouteKey(mailRouteFeedback);
      sendLog.setStatus(MailConstants.DELIVERING);
          //新增消息发送的日志记录
      mailSendLogService.save(sendLog);
      //③投递消息
      rabbitTemplate.convertAndSend(mailExchange,mailRouteFeedback,json,new CorrelationData(msgId));
   }
}
