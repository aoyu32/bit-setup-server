package com.aoyu.bitsetup.model.entity.ai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @ClassName：ChatConversation
 * @Author: aoyu
 * @Date: 2025-09-23 16:58
 * @Description: 对话会话实体类
 */
@Data
@TableName("ai_chat_conversations")  // 修正表名
public class ChatConversation {

    private Long id; // 会话ID（雪花ID）
    private Long uid; // 用户ID，关联user_info.uid
    private String title; // 会话标题
    private Long modelId; // 使用的AI模型ID，关联ai_models.id
    private String type; // 业务类型：general-通用聊天，code-编程助手，translate-翻译，creative-创意写作，learn-学习助手等
    private Integer totalMessages; // 总消息数
    private Integer totalTokens; // 总消耗tokens
    private Integer status; // 会话状态：1-活跃，2-已归档，3-已删除
    private Date lastMessageTime; // 最后消息时间
    private Date createTime; // 创建时间
    private Date updateTime; // 更新时间

}