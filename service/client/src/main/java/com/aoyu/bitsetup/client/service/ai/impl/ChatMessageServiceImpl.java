package com.aoyu.bitsetup.client.service.ai.impl;

import com.aoyu.bitsetup.client.mapper.ai.ChatConversationMapper;
import com.aoyu.bitsetup.client.mapper.ai.ChatMessageMapper;
import com.aoyu.bitsetup.client.service.ai.ChatMessageService;
import com.aoyu.bitsetup.common.enumeration.ResultCode;
import com.aoyu.bitsetup.common.exception.BusinessException;
import com.aoyu.bitsetup.common.utils.UUIDUtil;
import com.aoyu.bitsetup.model.entity.ai.ChatConversation;
import com.aoyu.bitsetup.model.entity.ai.ChatMessage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
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
        log.info("解析后的会话id：{}",parseConversationId);
        //判断会话id是否存在
        LambdaQueryWrapper<ChatConversation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatConversation::getId, parseConversationId)
                .eq(ChatConversation::getStatus,1);
        Long res = chatConversationMapper.selectCount(queryWrapper);
        if(res == 0){
            throw new BusinessException(ResultCode.EXCEPTION_CONVERSATION_ID);
        }
        //转换会话id


        List<ChatMessage> chatMessageList = messages.stream().map(message -> {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setConversationId(parseConversationId);
            chatMessage.setUid(1969732104690380801L);
            chatMessage.setContent(message.getText());
            chatMessage.setMessageType(1);
            return chatMessage;
        }).toList();

        chatMessageMapper.insert(chatMessageList);

    }

    @Override
    public List<Message> getChatMessages(String conversationId, Long uid) {

        //解析会话id
        long parseConversationId = UUIDUtil.parseUUID(conversationId);

        LambdaQueryWrapper<ChatMessage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatMessage::getConversationId, parseConversationId)
                .eq(ChatMessage::getUid, uid)
                .eq(ChatMessage::getIsDeleted,0);

        List<ChatMessage> chatMessageList = chatMessageMapper.selectList(queryWrapper);

        return chatMessageList.stream()
                .map(this::convertToAiMessage)
                .collect(Collectors.toList());
    }

    private Message convertToAiMessage(ChatMessage chatMessage) {
        // 根据消息类型（角色）创建对应的Message
        // 假设ChatMessage中有role字段标识消息角色
        String content = chatMessage.getContent() != null ? chatMessage.getContent() : "";

        switch (chatMessage.getMessageType()) {
            case 1:
                return new UserMessage(content);
            case 2:
                return new AssistantMessage(content);
            case 3:
                return new SystemMessage(content);
            default:
                // 默认作为用户消息处理
                return new UserMessage(content);
        }
    }
}
