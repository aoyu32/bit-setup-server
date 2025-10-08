package com.aoyu.bitsetup.model.entity.comment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName：CommunityComment
 * @Author: aoyu
 * @Date: 2025-10-07 00:22
 * @Description: 帖子评论实体
 */
@Data
@TableName("community_comment")
public class CommunityComment {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id; // 记录id（雪花ID）

    private Long postId; // 帖子id

    private Long uid; // 评论用户uid

    private Long parentId; // 父评论id（0表示顶级评论）

    private Long replyUid; // 回复的用户uid

    private String content; // 评论内容

    private Integer status; // 状态：0-删除，1-正常，2-审核中

    private LocalDateTime createTime; // 创建时间

    private LocalDateTime updateTime; // 更新时间

}