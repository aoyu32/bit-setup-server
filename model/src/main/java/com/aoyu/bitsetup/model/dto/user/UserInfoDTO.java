package com.aoyu.bitsetup.model.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: UserInfoDTO
 * @Author: aoyu
 * @Date: 2025-09-20 15:13
 * @Description: 用户信息DTO
 */
@Data
public class UserInfoDTO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long uid; // 用户id（雪花算法，业务使用）
    private String email; // 邮箱
    private String phone; // 手机号
    private String role; // 用户角色：user-普通用户，vip-会员用户，admin-管理员用户
    private String nickname; // 昵称
    private String avatar; // 头像url
    private Integer gender; // 性别：0-未知，1-男，2-女
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registerTime; // 注册时间
    private String bio; // 个人简介
    private String career; // 职业
    private Integer level; // 用户等级
    private String levelTitle; // 等级称号
    private Integer isDeveloper; // 是否开发者认证
    private Long experience; // 经验值
    private String province; // 省份
    private String city; // 城市
    private String county; // 区县
    private Integer points; // 可用积分（当前余额）

    //统计字段
    private Integer postCount;//发帖数
    private Integer viewCount;//获得的浏览数
    private Integer likeCount;//获得点赞数量
    private Integer commentCount;//获得评论数量
    private Integer followCount;//我的关注数量
    private Integer fansCount;//我的粉丝数据

}