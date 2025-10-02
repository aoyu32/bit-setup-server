package com.aoyu.bitsetup.model.dto.community;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @ClassName：PostRecommendDTO
 * @Author: aoyu
 * @Date: 2025-10-02 21:42
 * @Description: 推荐帖子DTO
 */

@Data
public class PostRecommendDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long pid;
    private String title;
    private String category;
}
