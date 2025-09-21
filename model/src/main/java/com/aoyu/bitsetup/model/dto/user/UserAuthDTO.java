package com.aoyu.bitsetup.model.dto.user;

import lombok.Data;

/**
 * @ClassName：UserAuthDTO
 * @Author: aoyu
 * @Date: 2025-09-20 17:08
 * @Description: 用户认证DTO
 */

@Data
public class UserAuthDTO {

    private Long uid;
    private String email;
    private String role;
    private String password;
}
