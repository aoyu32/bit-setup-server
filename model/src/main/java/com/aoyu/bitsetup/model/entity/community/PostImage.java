package com.aoyu.bitsetup.model.entity.community;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName：PostImage
 * @Author: aoyu
 * @Date: 2025-10-02 00:16
 * @Description: 帖子图片实体
 */
@Data
@TableName("community_post_image")
public class PostImage {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id; // 记录id（雪花ID）

    private Long postId; // 帖子id

    private Long uid; // 用户uid

    private String imageUrl; // 图片url

    private Integer sort; // 排序

    private Integer status; // 状态：0-删除，1-正常

    private LocalDateTime createTime; // 创建时间
}
