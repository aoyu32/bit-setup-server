package com.aoyu.bitsetup.client.service.ai.impl;

import com.aoyu.bitsetup.client.mapper.ai.ChatConversationMapper;
import com.aoyu.bitsetup.client.service.ai.ChatConversationService;
import com.aoyu.bitsetup.common.enumeration.ResultCode;
import com.aoyu.bitsetup.common.exception.BusinessException;
import com.aoyu.bitsetup.common.utils.UUIDUtil;
import com.aoyu.bitsetup.model.entity.ai.ChatConversation;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @ClassName：ChatSessionServiceImpl
 * @Author: aoyu
 * @Date: 2025-09-23 17:07
 * @Description: 聊天会话接口实现类
 */

@Slf4j
@Service
public class ChatConversationServiceImpl implements ChatConversationService {

    @Autowired
    private ChatConversationMapper chatConversationMapper;


    @Override
    public String getConversationId(Long uid) {
        //生成会话id
        long sessionId = IdWorker.getId();
        ChatConversation chatConversation = new ChatConversation();
        chatConversation.setId(sessionId);
        chatConversation.setUid(uid);
        int res = chatConversationMapper.insert(chatConversation);
        if (res == 0) {
            throw new BusinessException(ResultCode.CREATE_SESSION_ID_ERROR);
        }

        //返回会话uuid字符串
        String uuidString = UUIDUtil.generateUUID(sessionId);  // 现在返回字符串
        log.info("生成会话uuid：{}", uuidString);
        return uuidString;
    }

}
