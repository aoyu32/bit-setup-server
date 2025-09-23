package com.aoyu.bitsetup.client.ai;

import com.aoyu.bitsetup.client.service.ai.ChatMessageService;
import com.aoyu.bitsetup.common.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName：MysqlChatMemory
 * @Author: aoyu
 * @Date: 2025-09-23 16:18
 * @Description: mysql对话记忆存储类
 */

@Component
@Slf4j
public class MysqlChatMemory implements ChatMemory {

    @Autowired
    private ChatMessageService chatMessageService;

    @Override
    public void add(String conversationId, List<Message> messages) {
        chatMessageService.saveChatMessage(conversationId, messages);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {

        List<Message> chatMessages = chatMessageService.getChatMessages(conversationId, 1969732104690380801L);
        log.info("聊天记忆：{}",chatMessages.toString());
        return chatMessages;
    }


    @Override
    public void clear(String conversationId) {

    }
}
