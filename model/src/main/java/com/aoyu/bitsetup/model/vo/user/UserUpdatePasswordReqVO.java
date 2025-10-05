package com.aoyu.bitsetup.model.vo.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @ClassName：UserUpdatePasswordReqVO
 * @Author: aoyu
 * @Date: 2025-10-04 23:20
 * @Description: 用户请求更新密码vo
 */

@Data
public class UserUpdatePasswordReqVO {

    private Long uid;
    @NotNull(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z]{1,16}$", message = "密码必须为1~16位数字和字母组合")
    private String originalPassword;
    @NotNull(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z]{1,16}$", message = "密码必须为1~16位数字和字母组合")
    private String newPassword;

}
