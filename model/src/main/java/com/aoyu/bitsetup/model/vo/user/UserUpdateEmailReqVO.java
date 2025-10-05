package com.aoyu.bitsetup.model.vo.user;

import com.aoyu.bitsetup.common.interfaces.UserAuthVerifiable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @ClassName：UserUpdateEmailReqVO
 * @Author: aoyu
 * @Date: 2025-10-04 23:33
 * @Description: 用户更新邮箱VO
 */

@Data
public class UserUpdateEmailReqVO{
    private Long uid;

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


}
