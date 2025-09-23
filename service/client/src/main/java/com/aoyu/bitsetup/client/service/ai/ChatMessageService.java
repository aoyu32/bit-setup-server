package com.aoyu.bitsetup.client.service.ai;

import org.springframework.ai.chat.messages.Message;

import java.util.List;

/**
 * @InterfaceName：ChatMessageService
 * @Author: aoyu
 * @Date: 2025/9/23 下午7:45
 * @Description:
 */
public interface ChatMessageService {

    void saveChatMessage(String conversationId, List<Message> messages);

    List<Message> getChatMessages(String conversationId, Long uid);

}
