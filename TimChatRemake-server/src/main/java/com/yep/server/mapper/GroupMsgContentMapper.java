package com.yep.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yep.server.pojo.GroupMsgContent;
import org.springframework.stereotype.Repository;

/**
 * @author HuangSir
 * @date 2022-02-15 16:30
 */
@Repository
public interface GroupMsgContentMapper extends BaseMapper<GroupMsgContent> {
   int insert(GroupMsgContent groupMsgContent);
}
