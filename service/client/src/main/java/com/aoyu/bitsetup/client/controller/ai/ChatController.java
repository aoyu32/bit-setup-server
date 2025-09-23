package com.aoyu.bitsetup.client.controller.ai;

import com.aoyu.bitsetup.client.service.ai.ChatService;
import com.aoyu.bitsetup.model.vo.ai.ChatReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * @ClassName：ChatController
 * @Author: aoyu
 * @Date: 2025-09-22 12:33
 * @Description: ai聊天接口
 */

@Tag(name = "智能AI对话")
@RestController
@RequestMapping("/api/ai")
@Slf4j
public class ChatController {

    @Autowired
    private ChatService chatService;


    @Operation(description = "ai对话")
    @PostMapping(value = "/chat")
    public Flux<String> chat(@RequestBody ChatReqVO chatReqVO){
        log.info("用户uid为：{} ,会话id：{}请求聊天对话",chatReqVO.getUid(),chatReqVO.getConversationId());
        return chatService.chat(chatReqVO);
    }

}
