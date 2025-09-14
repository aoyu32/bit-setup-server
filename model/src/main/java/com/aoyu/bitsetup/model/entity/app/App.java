package com.aoyu.bitsetup.model.entity.app;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @ClassName：App
 * @Author: aoyu
 * @Date: 2025-09-14 14:30
 * @Description: 应用实体类
 */

@Data
@TableName("app")
public class App {

    // 雪花算法ID - 唯一标识
    private Long id;
    // 应用名称
    private String appName;
    // 应用大小(字节)
    private Long size;
    // 内存占用量(MB)
    private Integer memoryUsage;
    // 应用简介
    private String brief;
    // 分类ID
    private Integer categoryId;
    // 评分(0-5分)
    private BigDecimal rating;
    // 图标URL
    private String iconUrl;
    // 应用介绍(MD文档)
    private String introduction;
    // 安装教程(MD文档)
    private String installationGuide;
    // 付费制度(free, trial, one_time, subscription, in_app_purchase, open_source)
    private String pricingModel;
    // 是否绿色版/破解版：false-否，true-是
    private Boolean isCracked;
    // 官网地址
    private String officialWebsite;
    // 发布日期
    private Date releaseDate;
    // 更新日期
    private Date updateDate;
    // 软件来源(domestic-国产, foreign-国外)
    private String originRegion;
    // 是否个人开发：false-否，true-是
    private Boolean isPersonalDevelop;
    // 是否热门：false-否，true-是
    private Boolean isHot;
    // 是否推荐：false-否，true-是
    private Boolean isRecommend;
    // 是否最新：false-否，true-是
    private Boolean isNew;
    // 是否必备：false-否，true-是
    private Boolean isEssential;
    // 开发商
    private String developer;
    // 下载所需积分
    private Integer pointsRequired;
    // 是否支持中文：false-否，true-是
    private Boolean isChineseSupported;
    // 分享者ID
    private Long sharerId;
    // 状态：0-下架，1-上架
    private Integer status;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;


}
