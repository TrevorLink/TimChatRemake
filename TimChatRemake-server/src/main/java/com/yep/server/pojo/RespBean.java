package com.yep.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口返回的公共实体类
 * @author HuangSir
 * @date
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespBean {
  private Integer status;//状态码
  private String msg;//返回消息
  private Object obj;//返回实体

  public static RespBean build(){
    return new RespBean();
  }

  public static RespBean ok(String msg){
    return new RespBean(200,msg,null);
  }
  public static RespBean ok(String msg,Object obj){
    return new RespBean(200,msg,obj);
  }

  public static RespBean error(String msg){
    return new RespBean(500,msg,null);
  }
  public static RespBean error(String msg,Object obj){
    return new RespBean(500,msg,obj);
  }


}