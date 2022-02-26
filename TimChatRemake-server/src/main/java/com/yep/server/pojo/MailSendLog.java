package com.yep.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 邮件发送日志表
 *
 * @author HuangSir
 * @date 2022-02-21 10:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailSendLog implements Serializable {
   private static final long serialVersionUID = 740872026109078508L;

   private String msgId;
   /**
    * 1:反馈，2:验证码
    */
   private Integer contentType;

   private String content;

   private Integer status;

   private String routeKey;

   private String exchange;

   private Integer count;

   private Date tryTime;

   private Date createTime;

   private Date updateTime;
}
