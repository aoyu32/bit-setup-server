package com.aoyu.bitsetup.model.vo.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @ClassName：UserRegisterRespVO
 * @Author: aoyu
 * @Date: 2025-09-20 13:55
 * @Description: 应用注册响应VO
 */

@Data
public class UserRegisterRespVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long uid;
    private String token;
    private String avatar;
    private String role;
    private Integer points;

}
