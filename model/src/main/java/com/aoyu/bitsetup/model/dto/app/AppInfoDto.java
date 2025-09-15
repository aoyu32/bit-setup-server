package com.aoyu.bitsetup.model.dto.app;

import lombok.Data;

/**
 * @ClassName：AppInfoDto
 * @Author: aoyu
 * @Date: 2025-09-15 13:30
 * @Description: 应用基础信息类
 */

@Data
public class AppInfoDto {

    private Long id;
    private String appName;
    private String iconUrl;
    private String category;
    private Double rating;
    private String brief;
    private Boolean isPersonalDevelop;
    private Integer pointsRequired;
    private Boolean isNew;
    private Boolean isCracked;
    private Integer downloadCount;

}
