package com.aoyu.bitsetup.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName：ResultCode
 * @Author: aoyu
 * @Date: 2025-09-13 13:40
 * @Description: 响应状态码枚举类
 */


@AllArgsConstructor
@Getter
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    ERROR(500,"操作失败"),
    CAPTCHA_CODE_ERROR(501,"行为验证码错误"),
    ACCOUNT_UN_EXISTS(502, "账号不存在"),
    ACCOUNT_EXISTS(503, "账号已存在"),
    EMAIL_CAPTCHA_ERROR(504,"邮箱验证码错误"),
    REGISTER_ERROR(505, "注册出错"),
    TOKEN_UN_VALID(506, "登录认证失败"),
    TOKEN_EXPIRATION(507,"认证过期"),
    PASSWORD_INVALID(508, "账号或密码错误"),
    USER_UN_EXIST(509,"未知用户" ),
    UID_EXCEPTION(510, "用户ID错误"),
    //聊天会话
    CREATE_SESSION_ID_ERROR(511, "创建会话ID错误"),
    EXCEPTION_CONVERSATION_ID(512, "未知会话id"),
    UPDATE_CONVERSATION_STATUS_ERROR(513, "删除聊天会话失败"),
    USER_INFO_UPDATE_ERROR(514, "用户信息更新失败"),
    LOGIN_RECORD_ERROR(515, "用户登录记录信息错误"),
    NO_LOGIN_RECORD(516, "未查询到用户登录记录"),
    EMAIL_UPDATE_ERROR(517, "邮箱修改错误"),
    EMAIL_ALREADY_BINDING(518, "邮箱已被绑定"),
    ORIGINAL_PASSWORD_INVALID(519, "原密码错误"),
    NOT_AGREE_AGREEMENTS(520, "未同意注销条款"),
    ACCOUNT_DELETE_ERROR(521, "用户注销错误"),
    UPDATE_PASSWORD_ERROR(522, "密码修改错误"),
    SUBMIT_ERROR(523, "提交应用出错啦"),
    UNKNOWN_SUBMIT_TYPE(524,"未知投稿类型");


    private final int code;
    private final String msg;

}
