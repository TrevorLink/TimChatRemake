package com.yep.server.controller;

import com.yep.server.pojo.Feedback;
import com.yep.server.pojo.RespBean;
import com.yep.server.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuangSir
 * @date 2022-02-21 19:27
 */
@RestController
@RequestMapping("mail")
public class MailController {
   @Autowired
   private FeedbackService feedbackService;

   /**
    * 用户调用接口主动发送反馈表单
    * @param feedback
    * @return
    */
   @RequestMapping("feedback")
   public RespBean sendFeedbackToMail(@RequestBody Feedback feedback){
         feedbackService.sendFeedbackToMail(feedback);
         return RespBean.ok("邮件已经发送给管理员！感谢宁的反馈！");
   }
}
