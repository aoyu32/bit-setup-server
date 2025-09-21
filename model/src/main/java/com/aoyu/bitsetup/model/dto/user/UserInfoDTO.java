package com.aoyu.bitsetup.model.dto.user;

import lombok.Data;

/**
 * @ClassName：UserInfoDTO
 * @Author: aoyu
 * @Date: 2025-09-20 15:13
 * @Description: 用户信息DTO
 */

@Data
public class UserInfoDTO {

    private Long uid;
    private String nickname;
    private String avatar;
    private Integer gender;

}
