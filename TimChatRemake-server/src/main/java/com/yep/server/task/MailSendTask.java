package com.yep.server.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yep.server.pojo.MailConstants;
import com.yep.server.pojo.MailSendLog;
import com.yep.server.service.MailSendLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 定时任务，邮件发送任务
 * @author HuangSir
 * @date 2022-02-21 12:05
 */
@Component
public class MailSendTask {
   @Autowired
   private MailSendLogService mailSendLogService;
   @Autowired
   private RabbitTemplate rabbitTemplate;
   @Scheduled(cron = "0/10 * * * * ?")
   public void mailSendTask(){
      //TODO 这里每十秒钟就查询一次日志表，可以考虑使用视图和索引提高查询的效率
      //①获取到日志表中所有未正常投递的日志
      QueryWrapper<MailSendLog> wrapper = new QueryWrapper<>();
      wrapper.eq("status", MailConstants.DELIVERING);
      List<MailSendLog> sendLogs = mailSendLogService.list(wrapper);
      UpdateWrapper<MailSendLog> updateWrapper = new UpdateWrapper<>();
      //ForEach遍历集合
      sendLogs.forEach(mailSendLog -> {
         //②如果超过了最大尝试发送的次数还是发不出去就更新这条发送的日志的发送状态为发送失败！
         if (mailSendLog.getCount()>MailConstants.MAX_TRY_COUNT){
            updateWrapper.eq("msgId",mailSendLog.getMsgId());
            MailSendLog temp = new MailSendLog();
            temp.setStatus(MailConstants.FAILURE);
            mailSendLogService.update(temp, updateWrapper);
         }else{
            //③更新消息投递的尝试次数和时间
            updateWrapper.eq("msgId",mailSendLog.getMsgId());
            MailSendLog temp = new MailSendLog();
            temp.setTryTime(new Date());
            temp.setCount(mailSendLog.getCount()+1);
            mailSendLogService.update(temp,updateWrapper);
            //④获取到消息并再次投递
               String message = mailSendLog.getContent();
               rabbitTemplate.convertAndSend(mailSendLog.getExchange(),mailSendLog.getRouteKey(),message,new CorrelationData(mailSendLog.getMsgId()));
         }
      });
   }
}
