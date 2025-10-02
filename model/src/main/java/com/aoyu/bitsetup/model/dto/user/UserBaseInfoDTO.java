package com.aoyu.bitsetup.model.dto.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @ClassName：UserBaseInfoDTO
 * @Author: aoyu
 * @Date: 2025-10-02 09:10
 * @Description: 用户基本信息dto
 */

@Data
public class UserBaseInfoDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uid;
    private String nickname;
    private String avatar;
    private Integer level; // 用户等级
    private String levelTitle; // 等级称号
    private String role;

}
