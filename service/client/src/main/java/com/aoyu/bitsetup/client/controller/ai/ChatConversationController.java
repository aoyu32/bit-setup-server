package com.aoyu.bitsetup.client.controller.ai;

import com.aoyu.bitsetup.client.service.ai.ChatConversationService;
import com.aoyu.bitsetup.common.result.Result;
import com.aoyu.bitsetup.common.utils.ThreadLocalUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName：ChatSessionController
 * @Author: aoyu
 * @Date: 2025-09-23 17:30
 * @Description: 聊天会话
 */

@Tag(name = "聊天会话")
@RestController
@RequestMapping("/api/ai/conversation")
@Slf4j
public class ChatConversationController {

    @Autowired
    private ChatConversationService chatConversationService;

    @Operation(description = "获取新聊天会话id")
    @GetMapping("/new")
    public Result<String> getSessionId(){
        Long uid = (Long)ThreadLocalUtil.get("uid");
        log.info("用户uid为：{}请求获取新会话id",uid);
        String sessionId = chatConversationService.getConversationId(uid);
        return Result.success(sessionId);
    }



}
