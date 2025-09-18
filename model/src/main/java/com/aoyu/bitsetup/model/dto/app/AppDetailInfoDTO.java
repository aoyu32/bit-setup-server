package com.aoyu.bitsetup.model.dto.app;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName：AppDetailInfoDTO
 * @Author: aoyu
 * @Date: 2025-09-17 13:07
 * @Description: 应用详情DTO
 */

@Data
public class AppDetailInfoDTO {

    private Long appId;
    private String appName;
    private String appVersion;
    private String iconUrl;
    private String parentCategory;
    private String childCategory;
    private Long size;
    private List<String> platform;
    private Integer points;
    private Integer commentCount;
    private String pricingModel;
    private String developer;
    private List<String> tags;
    private Double rating;
    private Date createTime;
    private Integer downloadCount;
    private Integer collectCount;
    private String officialWebsite;
    private List<String> screenshots;
    private String introductionText;
    private String installGuide;
    private String downloadUrl;
    private List<AppVersionDTO> otherVersions;

}
