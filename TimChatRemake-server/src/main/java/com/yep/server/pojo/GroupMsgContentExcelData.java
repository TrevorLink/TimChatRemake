package com.yep.server.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yep.server.converter.MyUrlImageConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.Date;

/**
 * Excel导出所需要的实体类信息，把群聊内容切割为单独文字和单独图片
 * @author HuangSir
 * @date 2022-02-18 23:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMsgContentExcelData {
   @ExcelProperty("消息内容编号")
   private Integer id;
   @ExcelProperty("消息发送者编号")
   private  Integer fromId;
   @ExcelProperty("发消息的人的昵称")
   private String fromName;
   @ExcelIgnore//草 走 忽略！
   private URL fromProfile;
   @ExcelProperty("发送实现")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
   private Date createTime;
   @ExcelProperty(value = {"内容","文本"})
   @ColumnWidth(50)
   private String textContent;
   @ExcelProperty(value = {"内容","图片"})
   @ColumnWidth(50)
   private URL imageContent;
   @ExcelIgnore//这个也要忽略
   private Integer messageTypeId;
}
