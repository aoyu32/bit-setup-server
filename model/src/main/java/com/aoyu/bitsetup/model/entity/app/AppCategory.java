package com.aoyu.bitsetup.model.entity.app;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @ClassName：AppCategory
 * @Author: aoyu
 * @Date: 2025-09-16 13:17
 * @Description: 应用分类实体
 */
@Data
@TableName("app_category")
public class AppCategory {

    // 分类ID
    private Integer id;

    // 分类名称
    private String categoryName;

    // 分类编码
    private String categoryCode;

    // 父分类ID，NULL表示一级分类
    private Integer parentId;

    // 分类级别：1-一级分类，2-二级分类
    private Integer level;

    // 分类图标URL
    private String icon;

    // 分类描述
    private String description;

    // 排序序号，数字越小越靠前
    private Integer sortOrder;

    // 是否可见：0-隐藏，1-显示
    private Integer isVisible;

    // 分类下的应用数量
    private Integer appCount;

    // 创建时间
    private LocalDateTime createTime;

    // 更新时间
    private LocalDateTime updateTime;
}