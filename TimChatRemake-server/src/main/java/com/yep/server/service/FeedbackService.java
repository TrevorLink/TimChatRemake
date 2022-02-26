package com.yep.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yep.server.pojo.Feedback;

/**
 * @author HuangSir
 * @date 2022-02-21 12:04
 */
public interface FeedbackService extends IService<Feedback> {
   /**
    * 发送反馈信息
    * @param feedback
    */
   void sendFeedbackToMail(Feedback feedback);
}
