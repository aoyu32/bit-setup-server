package com.aoyu.bitsetup.model.query;

import lombok.Data;

/**
 * @ClassName：AppQuery
 * @Author: aoyu
 * @Date: 2025-09-15 14:27
 * @Description: 应用信息查询条件实体
 */
@Data
public class AppQuery extends PageQuery {

    private Integer FatherCategoryId;
    private Integer ChildCategoryId;
    private Double minRating;
    private Double maxRating;
    private Integer minDownload;
    private Integer maxDownload;
    private String platformName;
    private String pricingModel;
    private Long minSize;
    private Long maxSize;
    private Long minMemory;
    private Long maxMemory;
    private String installer;
    private Boolean isPersonalDevelop;
    private Boolean isHot;
    private Boolean isRecommend;
    private Boolean isEssential;
    private Boolean isNew;
    private Boolean isCracked;
    private Integer points;
    private String originRegion;

}