package com.yep.server.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yep.server.pojo.GroupMsgContent;
import com.yep.server.pojo.GroupMsgContentExcelData;
import com.yep.server.pojo.RespBean;
import com.yep.server.pojo.RespPageBean;
import com.yep.server.service.GroupMsgContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HuangSir
 * @date 2022-02-18 15:33
 */
@RestController
@RequestMapping("/groupMsgContent")
public class GroupMsgContentController {
   @Autowired
   private GroupMsgContentService groupMsgContentService;

   /**
    * 查看所有的群聊消息记录
    *
    * @return
    */
   @GetMapping("/")
   public List<GroupMsgContent> getAllGroupMsgContent() {
      return groupMsgContentService.list();
   }

   /**
    * 通过消息编号查询单条消息
    *
    * @param id
    * @return
    */
   @GetMapping("selectOne")
   public GroupMsgContent selectOne(Integer id) {
      return groupMsgContentService.getById(id);
   }

   /**
    * 可以根据发送者昵称，消息类型，发送的时间范围进行分页消息的查询
    *
    * @param page
    * @param size
    * @param nickname
    * @param type
    * @param dateScope
    * @return
    */
   @GetMapping("/page")
   public RespPageBean getAllGroupMsgContentByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                   @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                   String nickname, Integer type,
                                                   Date[] dateScope) {
      Page<GroupMsgContent> pageModel = new Page<>(page, size);
      QueryWrapper<GroupMsgContent> wrapper = new QueryWrapper<>();
      if (nickname != null) wrapper.like("from_name", nickname);
      if (type != null) wrapper.eq("message_type_id", type);
      if (dateScope != null) wrapper.between("create_time", dateScope[0], dateScope[1]);
      Page<GroupMsgContent> res = groupMsgContentService.page(pageModel, wrapper);
      long total = groupMsgContentService.count(wrapper);
      return new RespPageBean(total, res.getRecords());
   }

   /**
    * 单条消息记录删除
    *
    * @param id
    * @return
    */
   @DeleteMapping("/{id}")
   public RespBean deleteGroupMsgContentById(@PathVariable("id") Integer id) {
      boolean remove = groupMsgContentService.removeById(id);
      if (remove) return RespBean.ok("删除单条群聊消息成功！");
      else return RespBean.error("删除单条群聊消息失败！");
   }

   /**
    * 多条记录批量删除
    *
    * @param ids
    * @return
    */
   @DeleteMapping("/")
   public RespBean deleteGroupMsgContentByIds(Integer[] ids) {
      boolean remove = groupMsgContentService.removeByIds(Arrays.asList(ids));
      if (remove) return RespBean.ok("批量删除群聊消息成功！");
      else return RespBean.error("批量删除群聊消息失败！");
   }

   /**
    * 导出群聊记录为Excel文档
    * 针对聊天内容不进行特殊处理了，图片URL就写入Excel中了，速度太慢了
    * @param response
    */
   @GetMapping("/download")
   public void exportExcel(HttpServletResponse response) throws IOException {
      response.setContentType("application/vnd.ms-excel");
      response.setCharacterEncoding("utf-8");
      //设置文件信息，URLEncoder.encode可以防止中文乱码
      String fileName = URLEncoder.encode("群聊记录", "UTF-8").replaceAll("\\+", "%20");
      response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
      //我们先获取到所有的群聊记录，之后同意转化成为Excel专属的记录实体
      List<GroupMsgContent> groupMsgContents = getAllGroupMsgContent();
//      List<GroupMsgContentExcelData> collect = groupMsgContents.stream().map(item -> {
//         try {
//            return GroupMsgContent.convertToGroupMsgContentExcelData(item);
//         } catch (MalformedURLException e) {
//            e.printStackTrace();
//         }
//         return new GroupMsgContentExcelData();
//      }).collect(Collectors.toList());
      EasyExcel.write(response.getOutputStream(),GroupMsgContent.class).sheet("sheet1").doWrite(groupMsgContents);
   }
}
