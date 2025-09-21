package com.aoyu.bitsetup.common.exception;

import com.aoyu.bitsetup.common.enumeration.ResultCode;
import lombok.Data;

/**
 * @ClassName：BusinessException
 * @Author: aoyu
 * @Date: 2025-09-20 13:21
 * @Description: 业务异常处理类
 */

@Data
public class BusinessException extends RuntimeException{

    private Integer code;
    private String msg;

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    public BusinessException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

}
