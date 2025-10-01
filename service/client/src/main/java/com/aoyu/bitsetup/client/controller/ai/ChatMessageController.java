package com.aoyu.bitsetup.client.controller.ai;

import com.aoyu.bitsetup.client.service.ai.ChatMessageService;
import com.aoyu.bitsetup.common.result.Result;
import com.aoyu.bitsetup.model.vo.ai.ChatMessageRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName：ChatMessageController
 * @Author: aoyu
 * @Date: 2025-09-24 19:57
 * @Description: 聊天消息
 */

@Tag(name = "聊天消息")
@Slf4j
@RestController
@RequestMapping("/api/ai/message")
public class ChatMessageController {

    @Autowired
    ChatMessageService chatMessageService;

    @Operation(description = "获取会话聊天记录")
    @GetMapping("/all/{conversationId}")
    public Result<List<ChatMessageRespVO>> getMessage(@PathVariable String conversationId) {
       log.info("获取会话id为：{}的所有聊天消息",conversationId);
        List<ChatMessageRespVO> messageRespVOS = chatMessageService.getAllByConversationId(conversationId);
        return Result.success(messageRespVOS);
    }

}
