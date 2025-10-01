package com.aoyu.bitsetup.client.service.ai.impl;

import com.aoyu.bitsetup.client.mapper.ai.ChatConversationMapper;
import com.aoyu.bitsetup.client.service.ai.ChatConversationService;
import com.aoyu.bitsetup.common.enumeration.ResultCode;
import com.aoyu.bitsetup.common.exception.BusinessException;
import com.aoyu.bitsetup.common.utils.UUIDUtil;
import com.aoyu.bitsetup.model.entity.ai.ChatConversation;
import com.aoyu.bitsetup.model.vo.ai.ChatConversationRespVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<ChatConversationRespVO> getAllConversationId(Long uid) {

        LambdaQueryWrapper<ChatConversation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatConversation::getUid, uid).
                eq(ChatConversation::getStatus, 1)
                .orderByDesc(ChatConversation::getCreateTime);
        List<ChatConversation> chatConversations = chatConversationMapper.selectList(queryWrapper);
        return chatConversations.stream().map(chatConversation -> {
            ChatConversationRespVO chatConversationRespVO = new ChatConversationRespVO();
            chatConversationRespVO.setConversationId(UUIDUtil.generateUUID(chatConversation.getId()));
            chatConversationRespVO.setTitle(chatConversation.getTitle());
            return chatConversationRespVO;
        }).collect(Collectors.toList());
    }

    @Override
    public void updateTitle(Long id, String title) {
        log.info("请求更新标题的会话ID:{}", id);
        LambdaQueryWrapper<ChatConversation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ChatConversation::getId, id)
                .eq(ChatConversation::getStatus, 1);
        ChatConversation chatConversation = new ChatConversation();
        chatConversation.setTitle(title);
        chatConversationMapper.update(chatConversation, lambdaQueryWrapper);
    }

    @Override
    public void deleteConversation(String correlationId) {

        LambdaUpdateWrapper<ChatConversation> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        //解析id
        Long parsedConversationId = UUIDUtil.parseUUID(correlationId);
        lambdaUpdateWrapper.eq(ChatConversation::getId, parsedConversationId)
                .eq(ChatConversation::getStatus, 1)
                .set(ChatConversation::getStatus, 0);
        int res = chatConversationMapper.update(null,lambdaUpdateWrapper);
        if (res == 0) {
            throw new BusinessException(ResultCode.UPDATE_CONVERSATION_STATUS_ERROR);
        }


    }

}
