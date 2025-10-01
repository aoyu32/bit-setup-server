package com.aoyu.bitsetup.client.service.ai;

import com.aoyu.bitsetup.model.vo.ai.ChatConversationRespVO;

import java.util.List;

/**
 * @InterfaceName：ChatSessionService
 * @Author: aoyu
 * @Date: 2025/9/23 下午5:06
 * @Description:
 */

public interface ChatConversationService {

    String getConversationId(Long uid);

    List<ChatConversationRespVO> getAllConversationId(Long uid);

    void updateTitle (Long uid, String title);

    void deleteConversation(String correlationId);

}
