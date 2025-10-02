package com.aoyu.bitsetup.model.entity.community;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName：CommunityPost
 * @Author: aoyu
 * @Date: 2025-10-01 14:12
 * @Description: 社区帖子实体
 */

@Data
@TableName("community_post")
public class Post {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id; // 记录id（雪花ID）

    private Long uid; // 用户uid（作者）

    private Long categoryId; // 分类id

    private String title; // 帖子标题

    private String summary; // 帖子摘要

    private String content; // 内容（markdown格式）

    private String coverUrl; // 封面图url

    private Date publishTime; // 发表时间

    private Date updateTime; // 更新时间

    private Integer status; // 状态：0-草稿，1-已发布，2-审核中，3-审核失败，4-已删除

    private Integer isTop; // 是否置顶：0-否，1-是

    private Integer isHot; // 是否是热帖：0-否，1-是

    private Integer isRecommend; // 是否推荐：0-否，1-是

    private Date createTime; // 创建时间
}
