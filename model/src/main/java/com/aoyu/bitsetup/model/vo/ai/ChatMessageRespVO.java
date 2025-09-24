package com.aoyu.bitsetup.model.vo.ai;

import lombok.Data;

/**
 * @ClassName：ChatMessageVO
 * @Author: aoyu
 * @Date: 2025-09-24 19:47
 * @Description: 聊天消息VO
 */

@Data
public class ChatMessageRespVO {

    private String content;
    private String role;

}
