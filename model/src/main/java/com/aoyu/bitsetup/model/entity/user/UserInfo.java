package com.aoyu.bitsetup.model.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName：UserInfo
 * @Author: aoyu
 * @Date: 2025-09-20 12:46
 * @Description: 用户信息类实体
 */
@Data
@TableName("user_info")
public class UserInfo {


    @TableId(type = IdType.ASSIGN_ID)
    private Long id; // 记录id（雪花ID）

    private Long uid; // 用户id（雪花算法，业务使用）

    private String nickname; // 昵称

    private String avatar; // 头像url

    private Integer gender; // 性别：0-未知，1-男，2-女

    private Date registerTime; // 注册时间

    private String registerIp; // 注册ip

    private String bio; // 个人简介

    private String occupation; // 职业

    private Integer level; // 用户等级

    private String levelTitle; // 等级称号

    private Date lastActiveTime; // 最后一次活跃时间

    private Integer isDeveloper; // 是否开发者认证

    private Long experience; // 经验值

    private String province; // 省份

    private String city; // 城市

    private String county; // 区县

    private Date createTime; // 记录创建时间

    private Date updateTime; // 记录更新时间
}
