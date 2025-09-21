package com.aoyu.bitsetup.model.vo.user;

import com.aoyu.bitsetup.common.interfaces.UserAuthVerifiable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @ClassName：UserLoginReqVO
 * @Author: aoyu
 * @Date: 2025-09-21 16:52
 * @Description: 用户登录请求VO
 */

@Data
public class UserLoginReqVO implements UserAuthVerifiable {
    /**
     * 滑块验证二次校验码
     */
    @NotNull(message = "行为验证码不能为空")
    private String verifyCode;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String account;

    /**
     * 密码,1~16位，数字字母组合
     */
    @NotNull(message = "密码不能为空")
    private String password;

    @Override
    public String getCaptchaCode() {
        return this.verifyCode;
    }

}
