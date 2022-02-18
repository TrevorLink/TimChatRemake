package com.yep.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yep.server.pojo.GroupMsgContent;
import com.yep.server.pojo.RespBean;
import com.yep.server.pojo.RespPageBean;
import com.yep.server.service.GroupMsgContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
    * @param ids
    * @return
    */
   @DeleteMapping("/")
   public RespBean deleteGroupMsgContentByIds( Integer[] ids) {
      boolean remove = groupMsgContentService.removeByIds(Arrays.asList(ids));
      if (remove) return RespBean.ok("批量删除群聊消息成功！");
      else return RespBean.error("批量删除群聊消息失败！");
   }
}
