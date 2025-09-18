package com.aoyu.bitsetup.model.dto.app;

import lombok.Data;

/**
 * @ClassName：AppRelatedDTO
 * @Author: aoyu
 * @Date: 2025-09-18 08:23
 * @Description: 相关应用推荐DTO
 */
@Data
public class AppRelatedDTO {
    private Long appId;
    private String iconUrl;
    private String appName;
    private Integer downloadCount;
    private Integer size;
}
