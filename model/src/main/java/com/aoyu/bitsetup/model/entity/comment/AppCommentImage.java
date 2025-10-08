package com.aoyu.bitsetup.model.entity.comment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName：AppCommentImage
 * @Author: aoyu
 * @Date: 2025-10-07 00:19
 * @Description: 应用评论附图实体
 */
@Data
@TableName("app_comment_image")
public class AppCommentImage {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id; // 雪花算法ID

    private Long commentId; // 评论ID

    private String imageUrl; // 图片URL

    private Integer sortOrder; // 排序序号

    private Integer width; // 图片宽度

    private Integer height; // 图片高度

    private Integer fileSize; // 文件大小(字节)

    private String format; // 图片格式：jpg, png, webp等

    private Integer isDeleted; // 是否删除

    private LocalDateTime createTime; // 创建时间
}
