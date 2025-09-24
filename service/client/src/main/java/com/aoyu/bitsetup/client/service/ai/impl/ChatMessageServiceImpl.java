package com.aoyu.bitsetup.client.service.ai.impl;

import com.aoyu.bitsetup.client.mapper.ai.ChatConversationMapper;
import com.aoyu.bitsetup.client.mapper.ai.ChatMessageMapper;
import com.aoyu.bitsetup.client.service.ai.ChatMessageService;
import com.aoyu.bitsetup.common.enumeration.ResultCode;
import com.aoyu.bitsetup.common.exception.BusinessException;
import com.aoyu.bitsetup.common.utils.UUIDUtil;
import com.aoyu.bitsetup.model.entity.ai.ChatConversation;
import com.aoyu.bitsetup.model.entity.ai.ChatMessage;
import com.aoyu.bitsetup.model.vo.ai.ChatConversationRespVO;
import com.aoyu.bitsetup.model.vo.ai.ChatMessageRespVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName：ChatMessageServiceImpl
 * @Author: aoyu
 * @Date: 2025-09-23 19:47
 * @Description: 聊天消息服务层接口实现类
 */

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private ChatConversationMapper chatConversationMapper;

    @Override
    public void saveChatMessage(String conversationId, List<Message> messages) {

        long parseConversationId = UUIDUtil.parseUUID(conversationId);
        log.info("解析后的会话id：{}", parseConversationId);
        log.info("聊天消息：{}", messages);
        //判断会话id是否存在
        LambdaQueryWrapper<ChatConversation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatConversation::getId, parseConversationId)
                .eq(ChatConversation::getStatus, 1);
        ChatConversation chatConversation = chatConversationMapper.selectOne(queryWrapper);
        if (chatConversation == null) {
            throw new BusinessException(ResultCode.EXCEPTION_CONVERSATION_ID);
        }
        //转换会话id
        List<ChatMessage> chatMessageList = messages.stream().map(message -> {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setConversationId(parseConversationId);
            chatMessage.setUid(chatConversation.getUid());
            chatMessage.setContent(message.getText());
            MessageType messageType = message.getMessageType();
            chatMessage.setMessageType(messageType.getValue());
            return chatMessage;
        }).toList();

        chatMessageMapper.insert(chatMessageList);

    }

    @Override
    public List<Message> getChatMessages(String conversationId) {

        //解析会话id
        long parseConversationId = UUIDUtil.parseUUID(conversationId);

        LambdaQueryWrapper<ChatMessage> queryWrapper = new LambdaQueryWrapper<>();
        log.info("查询聊天消息时解析后的id:{}", parseConversationId);
        queryWrapper.eq(ChatMessage::getConversationId, parseConversationId)
                .eq(ChatMessage::getIsDeleted, 0);

        List<ChatMessage> chatMessageList = chatMessageMapper.selectList(queryWrapper);
        log.info("查询到的会话消息记录：{}", chatMessageList);
        return chatMessageList.stream()
                .map(this::convertToAiMessage)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatMessageRespVO> getAllByConversationId(String conversationId) {
        List<Message> chatMessages = this.getChatMessages(conversationId);
        return chatMessages.stream().map(message -> {
            ChatMessageRespVO chatMessageRespVO = new ChatMessageRespVO();
            chatMessageRespVO.setRole(message.getMessageType().getValue());
            chatMessageRespVO.setContent(message.getText());
            return chatMessageRespVO;
        }).collect(Collectors.toList());
    }

    private Message convertToAiMessage(ChatMessage chatMessage) {
        // 根据消息类型（角色）创建对应的Message
        // 假设ChatMessage中有role字段标识消息角色
        String content = chatMessage.getContent() != null ? chatMessage.getContent() : "";

        return switch (chatMessage.getMessageType()) {
            case "user" -> new UserMessage(content);
            case "assistant" -> new AssistantMessage(content);
            case "system" -> new SystemMessage(content);
            default ->
                // 默认作为用户消息处理
                    new UserMessage(content);
        };
    }
}
