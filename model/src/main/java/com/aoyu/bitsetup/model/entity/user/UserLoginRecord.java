package com.aoyu.bitsetup.model.entity.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @ClassName：UserLoginRecord
 * @Author: aoyu
 * @Date: 2025-10-04 13:47
 * @Description: 用户登录记录实体
 */
@Data
@TableName("user_login_record")
public class UserLoginRecord implements Serializable {

    @TableId
    private Long id; // 记录id（雪花ID）

    private Long uid; // 用户id

    private Date loginTime; // 登录时间

    private String loginIp; // 登录IP

    private String ipProvince; // IP省份

    private String ipCity; // IP城市

    private String userAgent; // 用户代理

    private String deviceType; // 设备类型

    private String browser; // 浏览器

    private String os; // 操作系统

    private  Date createTime; // 创建时间
}
