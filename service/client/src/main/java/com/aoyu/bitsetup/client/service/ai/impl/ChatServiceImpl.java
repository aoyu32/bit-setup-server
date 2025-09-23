package com.aoyu.bitsetup.client.service.ai.impl;

import com.aoyu.bitsetup.client.service.ai.ChatService;
import com.aoyu.bitsetup.model.vo.ai.ChatReqVO;
import com.aoyu.bitsetup.model.vo.ai.ChatRespVO;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
/**
 * @ClassName：ChatServiceImpl
 * @Author: aoyu
 * @Date: 2025-09-23 14:30
 * @Description: ai聊天服务层接口
 */

@Service
public class ChatServiceImpl implements ChatService {


    @Autowired
    private ChatClient chatClient;


    @Override
    public Flux<String> chat(ChatReqVO chatReqVO) {

        return chatClient.prompt()
                .user(chatReqVO.getContent())
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatReqVO.getConversationId()))
                .stream()
                .content();

    }
}
