package com.aoyu.bitsetup.model.entity.submit;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.sql.Timestamp;

/**
 * @ClassName：AppSubmit
 * @Author: aoyu
 * @Date: 2025-10-05 13:38
 * @Description: 应用投稿实体
 */
@Data
@TableName("app_submit")
public class AppSubmit {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id; // 雪花算法ID（主键）

    private Long appId; // 关联的应用ID（审核通过后填入）
    private Long uid; // 用户ID
    private String appName; // 应用名称
    private Integer platformId; // 平台ID（关联platform.id）
    private String brief; // 应用简介
    private String introduction; // 应用详细介绍
    private Integer categoryId; // 分类ID
    private String pricingModel; // 付费制度
    private String iconUrl; // 图标URL
    private String downloadUrl; // 下载地址
    private Long fileSize; // 应用大小(字节)
    private String version; // 版本号
    private String officialWebsite; // 官网地址
    private Boolean isPersonalDevelop; // 是否个人开发：0-否，1-是
    private Boolean isDraft; // 是否草稿：0-否，1-是
    private Integer status; // 状态：0-待审核，1-已通过，2-已拒绝，3-需要修改
    private String reason; // 审核意见/拒绝原因
    private Timestamp reviewTime; // 审核时间
    private Long reviewerId; // 审核人ID
    private Integer pointsReward; // 投稿积分奖励
    private Integer expReward; // 投稿经验值奖励
    private Boolean isRewarded; // 是否已奖励
    private String ipAddress; // 投稿IP地址
    private Boolean isCanceled; // 是否已撤销
    private Timestamp createTime; // 创建时间
    private Timestamp updateTime; // 更新时间
}
