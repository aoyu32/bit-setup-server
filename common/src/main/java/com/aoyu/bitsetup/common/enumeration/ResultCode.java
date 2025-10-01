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
    UPDATE_CONVERSATION_STATUS_ERROR(513, "删除聊天会话失败");



    private final int code;
    private final String msg;

}
