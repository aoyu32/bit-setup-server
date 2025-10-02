package com.aoyu.bitsetup.model.dto.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @ClassName：UserDetailInfoDTO
 * @Author: aoyu
 * @Date: 2025-10-02 14:18
 * @Description: 用户详细信息DTO
 */

@Data
public class UserDetailInfoDTO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long uid;
    private String nickname;
    private String avatar;
    private Integer level; // 用户等级
    private String levelTitle; // 等级称号
    private String role;
    private String bio;
    //统计字段
    private Integer postCount;//发帖数
    private Integer likeCount;//获得点赞数量
    private Integer followCount;//我的关注数量
    private Integer fansCount;//我的粉丝数据


}
