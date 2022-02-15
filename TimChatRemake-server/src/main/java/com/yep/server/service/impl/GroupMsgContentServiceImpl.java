package com.yep.server.service.impl;

import com.yep.server.mapper.GroupMsgContentMapper;
import com.yep.server.pojo.GroupMsgContent;
import com.yep.server.service.GroupMsgContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HuangSir
 * @date 2022-02-15 16:32
 */
@Service
public class GroupMsgContentServiceImpl implements GroupMsgContentService {
   @Autowired
   private GroupMsgContentMapper groupMsgContentMapper;
   @Override
   public int insert(GroupMsgContent groupMsgContent) {
      return groupMsgContentMapper.insert(groupMsgContent);
   }
}
