package com.aoyu.bitsetup.model.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName：UserAuth
 * @Author: aoyu
 * @Date: 2025-09-20 12:44
 * @Description: 用户认证类实体
 */

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户认证信息表实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_auth")
public class UserAuth {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id; // 记录id（雪花ID）

    private Long uid; // 用户id（与user_info表uid关联）

    private String username; // 用户名

    private String email; // 邮箱

    private String phone; // 手机号

    private String role; // 用户角色：user-普通用户，vip-会员用户，admin-管理员用户

    private String password; // 登录密码

    private Integer status; // 账号状态：0-禁用，1-正常，2-注销

    private Date lastLoginTime; // 最近一次登录时间

    private String accessIp; // 访问ip

    private Integer isDeleted; // 是否逻辑删除

    private Date createTime; // 记录创建时间

    private Date updateTime; // 记录更新时间
}
