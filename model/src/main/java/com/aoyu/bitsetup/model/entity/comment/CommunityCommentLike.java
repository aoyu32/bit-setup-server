package com.aoyu.bitsetup.model.entity.comment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName：CommunityCommentLike
 * @Author: aoyu
 * @Date: 2025-10-07 00:22
 * @Description: 评论点赞记录实体
 */
@Data
@TableName("community_comment_like")
public class CommunityCommentLike {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id; // 记录id（雪花ID）

    private Long commentId; // 评论id

    private Long uid; // 点赞用户uid

    private Integer status; // 状态：0-已取消，1-有效

    private LocalDateTime createTime; // 点赞时间

    private LocalDateTime updateTime; // 更新时间

}