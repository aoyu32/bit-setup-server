package com.aoyu.bitsetup.model.vo.user;

import com.aoyu.bitsetup.common.interfaces.UserAuthVerifiable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @ClassName：UserRegisterReqVO
 * @Author: aoyu
 * @Date: 2025-09-20 13:54
 * @Description: 应用请求注册VO
 */

@Data
public class UserRegisterReqVO implements UserAuthVerifiable {

    /**
     * 滑块验证二次校验码
     */
    @NotNull(message = "行为验证码不能为空")
    private String verifyCode;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 邮箱验证码，1~6位数字
     */
    @Pattern(regexp = "^\\d{6}$", message = "邮箱验证码必须为6位数字")
    @NotNull(message = "邮箱验证码不能为空")
    private String emailVerifyCode;

    /**
     * 密码,1~16位，数字字母组合
     */
    @NotNull(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z]{1,16}$", message = "密码必须为1~16位数字和字母组合")
    private String password;

    @Override
    public String getCaptchaCode() {
        return this.verifyCode;
    }

    @Override
    public String getAccount() {
        return this.email;
    }
}
