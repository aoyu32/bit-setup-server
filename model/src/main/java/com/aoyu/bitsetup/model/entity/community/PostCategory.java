package com.aoyu.bitsetup.model.entity.community;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName：PostCategory
 * @Author: aoyu
 * @Date: 2025-10-01 14:15
 * @Description: 帖子类别实体
 */

@Data
@TableName("community_category")
public class PostCategory {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id; // 记录id（雪花ID）

    private String name; // 分类名称

    private String code;//分类编号

    private String description; // 分类描述

    private String icon; // 分类图标

    private Long parentId; // 父分类id（0表示顶级分类）

    private Integer level; // 分类层级：1-一级分类，2-二级分类

    private Integer sort; // 排序权重

    private Integer status; // 状态：0-禁用，1-启用

    private Date createTime; // 创建时间

    private Date updateTime; // 更新时间

}
