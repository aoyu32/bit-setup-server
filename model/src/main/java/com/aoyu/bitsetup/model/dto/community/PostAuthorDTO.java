package com.aoyu.bitsetup.model.dto.community;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @ClassName：PostAuthDTO
 * @Author: aoyu
 * @Date: 2025-10-02 20:25
 * @Description: 优秀帖子作者DTO
 */

@Data
public class PostAuthorDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uid;
    private String avatar;
    private String nickname;
    private Integer postCount;
    private Integer fansCount;
    private Integer likeCount;

}
