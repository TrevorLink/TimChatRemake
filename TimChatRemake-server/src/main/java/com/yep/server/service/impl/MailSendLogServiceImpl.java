package com.yep.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yep.server.mapper.MailSendLogMapper;
import com.yep.server.pojo.MailSendLog;
import com.yep.server.service.MailSendLogService;
import org.springframework.stereotype.Service;

/**
 * @author HuangSir
 * @date 2022-02-21 10:52
 */
@Service
public class MailSendLogServiceImpl extends ServiceImpl<MailSendLogMapper, MailSendLog> implements MailSendLogService {
}
