package com.yep.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户消息反馈
 * @author HuangSir
 * @date 2022-02-21 12:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feedback implements Serializable {
   private static final long serialVersionUID = 550979088670747783L;

   private String id;

   private String userId;

   private String username;

   private String nickname;

   private String content;

}
