package com.yep.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yep.server.pojo.GroupMsgContent;

/**
 * @author HuangSir
 * @date 2022-02-15 16:31
 */
public interface GroupMsgContentService extends IService<GroupMsgContent> {
   int insert(GroupMsgContent groupMsgContent);
}
