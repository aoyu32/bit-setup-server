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
    ERROR(500,"操作失败");

    private final int code;
    private final String msg;

}
