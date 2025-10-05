package com.aoyu.bitsetup.model.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName：UserBaseRespVO
 * @Author: aoyu
 * @Date: 2025-09-21 16:08
 * @Description: 用户信息响应基础VO
 */

@Data
public class UserBaseRespVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long uid;
    private String nickname;
    private String avatar;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Integer points;
    private String role;
    private String token;

}
