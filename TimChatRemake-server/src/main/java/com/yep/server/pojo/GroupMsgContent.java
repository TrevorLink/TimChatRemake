package com.yep.server.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 群聊消息
 * @author HuangSir
 * @date 2022-02-15 16:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMsgContent implements Serializable {
   private static final long serialVersionUID = 980328865610261046L;
   /**
    * 消息内容编号
    */
   private Integer id;
   /**
    * 发送者的编号
    */
   private Integer fromId;
   /**
    * 发送者的昵称
    */
   private String fromName;
   /**
    * 发送者的头像
    */
   private String fromProfile;
   /**
    * 消息发送时间
    */
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
   private Date createTime;
   /**
    * 消息内容
    */
   private String content;
   /**
    * 消息类型编号
    */
   private Integer messageTypeId;
}
