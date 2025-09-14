package com.aoyu.bitsetup.common.result;

import com.aoyu.bitsetup.common.enumeration.ResultCode;
import lombok.Data;

/**
 * @ClassName：Result
 * @Author: aoyu
 * @Date: 2025-09-13 13:28
 * @Description: 统一结果返回实体
 */

@Data
public class Result<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 成功响应
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 带数据的成功响应
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }

    /**
     * 失败响应
     */
    public static <T> Result<T> error(ResultCode resultCode) {
        Result<T> result = new Result<>();
        result.setCode(resultCode.getCode());
        result.setMessage(resultCode.getMsg());
        return result;
    }

    /**
     * 自定义错误消息的失败响应
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 带数据的错误返回结果
     * @param resultCode 响应结果枚举
     * @param data 返回的错误数据
     * @return result
     * @param <T> 任意类型
     */
    public static <T> Result<T> error(ResultCode resultCode,T data) {
        Result<T> result = new Result<>();
        result.setCode(resultCode.getCode());
        result.setMessage(resultCode.getMsg());
        result.setData(data);
        return result;
    }

}
