package com.aoyu.bitsetup.model.entity.ai;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName：ChatMessage
 * @Author: aoyu
 * @Date: 2025-09-23 17:05
 * @Description: 聊天消息实体
 */
@Data
@TableName("ai_chat_messages")  // 添加表名注解
public class ChatMessage {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id; // 消息ID（雪花ID）
    private Long conversationId; // 会话ID，关联ai_chat_conversations.id
    private Long uid; // 用户ID，关联user_info.uid
    private Integer messageType; // 消息类型：1-用户提问，2-AI回复，3-系统消息
    private String content; // 消息内容
    private Integer tokensUsed; // 消耗的tokens数量
    private Long modelId; // AI模型ID，关联ai_models.id（修正字段名）
    private Integer messageOrder; // 消息在会话中的顺序
    private Integer isDeleted; // 是否删除：0-否，1-是
    private Integer responseTime; // 响应时间（毫秒）
    private JsonNode messageMetadata; // 消息元数据（JSON格式）
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间

}