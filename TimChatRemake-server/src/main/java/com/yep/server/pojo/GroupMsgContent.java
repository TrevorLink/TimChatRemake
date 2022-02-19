package com.yep.server.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yep.server.converter.MyContentConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
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
   @ExcelProperty("消息内容编号")
   private Integer id;
   /**
    * 发送者的编号
    */
   @ExcelProperty("发送消息者的编号")
   private Integer fromId;
   /**
    * 发送者的昵称
    */
   @ExcelProperty("发送者的昵称")
   private String fromName;
   /**
    * 发送者的头像
    */
   @ExcelIgnore
   private String fromProfile;
   /**
    * 消息发送时间
    */
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
   @ExcelProperty("消息发送时间")
   private Date createTime;
   /**
    * 消息内容
    */
   @ExcelProperty(value = "消息发送内容")
   @ColumnWidth(50)
   private String content;
   /**
    * 消息类型编号
    */
   @ExcelIgnore
   private Integer messageTypeId;

   /**
    * 将数据库实体转化为Excel的数据实体
    * @param groupMsgContent
    * @return
    * @throws MalformedURLException
    */
   public static  GroupMsgContentExcelData convertToGroupMsgContentExcelData(GroupMsgContent groupMsgContent) throws MalformedURLException {
      GroupMsgContentExcelData excelData = new GroupMsgContentExcelData();
      excelData.setFromId(groupMsgContent.getFromId());
      excelData.setId(groupMsgContent.getId());
      excelData.setFromName(groupMsgContent.getFromName());
      excelData.setCreateTime(groupMsgContent.getCreateTime());
      //发送者头像转化为URL以Excel导出图片
      excelData.setFromProfile(new URL(groupMsgContent.getFromProfile()));
      if(groupMsgContent.getMessageTypeId()==1) excelData.setTextContent(groupMsgContent.getContent());
      //如果这一条群聊消息内容是图片我们就转化为URL导出
      if(groupMsgContent.getMessageTypeId()==2) excelData.setImageContent(new URL(groupMsgContent.getContent()));
      return  excelData;
   }
}
