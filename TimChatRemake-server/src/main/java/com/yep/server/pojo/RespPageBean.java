package com.yep.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//分页实体类，用于作为JSON对象返回给前端
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespPageBean {
  private Long total;//数据总数
  private List<?> data;//数据实体列表
}