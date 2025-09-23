package com.aoyu.bitsetup.model.vo.ai;

import lombok.Data;

/**
 * @ClassName：ChatReqVO
 * @Author: aoyu
 * @Date: 2025-09-23 19:36
 * @Description: 聊天请求VO
 */

@Data
public class ChatReqVO {

    private String uid;//用户uid
    private String conversationId;//会话uid
    private String content;//聊天内容

}
