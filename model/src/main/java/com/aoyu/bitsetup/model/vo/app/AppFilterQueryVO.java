package com.aoyu.bitsetup.model.vo.app;

import lombok.Data;

/**
 * @ClassName：AppFilterQueryVO
 * @Author: aoyu
 * @Date: 2025-09-16 20:28
 * @Description: 应用过滤查询VO
 */

@Data
public class AppFilterQueryVO {
    private Integer pageNum;
    private Integer pageSize;

    /**
     * 应用分类
     */
    private Integer category;

    /**
     * 子分类ID
     */
    private Integer subCategory;

    /**
     * 应用评分范围（格式：最低分-最高分，如：4.0-5.0）
     */
    private String rating;

    /**
     * 下载量级别（格式：最小下载量-最大下载量）
     */
    private String downloadLevel;

    /**
     * 平台类型（如：android, ios, windows等）
     */
    private String platform;

    /**
     * 应用大小范围（格式：最小大小-最大大小，单位：MB）
     */
    private String size;

    /**
     * 付费模式（如：free, paid, freemium等）
     */
    private String paymentModel;

    /**
     * 内存使用量范围（格式：最小使用量-最大使用量，单位：MB）
     */
    private String memoryUsage;

    /**
     * 安装方式
     */
    private String installMethod;

    /**
     * 其他查询字段
     */
    private String other;

}
