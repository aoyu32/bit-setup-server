package com.aoyu.bitsetup.model.dto.community;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @ClassName：PostHotDTO
 * @Author: aoyu
 * @Date: 2025-10-02 20:22
 * @Description: 热帖DTO
 */

@Data
public class PostHotDTO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long pid;
    private String category;
    private String title;
    private Integer viewCount;

}
