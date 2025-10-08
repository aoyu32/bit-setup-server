package com.aoyu.bitsetup.model.entity.comment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName：AppCommentLike
 * @Author: aoyu
 * @Date: 2025-10-07 00:20
 * @Description: 评论点赞记录表
 */
@Data
@TableName("app_comment_like")
public class AppCommentLike {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id; // 雪花算法ID

    private Long commentId; // 评论ID

    private Long uid; // 用户ID

    private Integer isActive; // 是否有效：0-取消，1-有效

    private LocalDateTime createTime; // 创建时间

    private LocalDateTime updateTime; // 更新时间

    private LocalDateTime cancelTime; // 取消时间
}
