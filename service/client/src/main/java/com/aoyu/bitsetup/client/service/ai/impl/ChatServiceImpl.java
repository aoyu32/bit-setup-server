package com.aoyu.bitsetup.client.service.ai.impl;

import com.aoyu.bitsetup.client.mapper.ai.ChatMessageMapper;
import com.aoyu.bitsetup.client.service.ai.ChatConversationService;
import com.aoyu.bitsetup.client.service.ai.ChatService;
import com.aoyu.bitsetup.common.utils.UUIDUtil;
import com.aoyu.bitsetup.model.entity.ai.ChatMessage;
import com.aoyu.bitsetup.model.vo.ai.ChatReqVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
/**
 * @ClassName：ChatServiceImpl
 * @Author: aoyu
 * @Date: 2025-09-23 14:30
 * @Description: ai聊天服务层接口
 */

@Service
@Slf4j
public class ChatServiceImpl implements ChatService {


    @Autowired
    private ChatClient chatClient;

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Qualifier("chatClientNoMemory")
    @Autowired
    private ChatClient chatClientNoMemory;

    @Autowired
    private ChatConversationService chatConversationService;

    @Override
    public Flux<String> chat(ChatReqVO chatReqVO) {

        return chatClient.prompt()
                .user(chatReqVO.getContent())
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatReqVO.getConversationId()))
                .stream()
                .content();
    }

    @Override
    public String generateTitle(String conversationId) {
        //查询第一条对话
        long parseConversationId = UUIDUtil.parseUUID(conversationId);
        log.info("解析后的会话id{}",parseConversationId);
        LambdaQueryWrapper<ChatMessage>  lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ChatMessage::getConversationId, parseConversationId)
                .in(ChatMessage::getMessageType, Arrays.asList(1,2))
                .eq(ChatMessage::getIsDeleted,0)
                .orderByAsc(ChatMessage::getCreateTime)
                .last("limit 2");
        List<ChatMessage> chatMessageList = chatMessageMapper.selectList(lambdaQueryWrapper);
        log.info("对话历史：{}",chatMessageList);

        String conversationTitle = chatClientNoMemory.prompt()
                .user("user："+chatMessageList.get(0).getContent())
                .call()
                .content();

        //更新标题
        chatConversationService.updateTitle(parseConversationId,conversationTitle);

        return conversationTitle;
    }
}
