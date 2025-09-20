package com.aoyu.bitsetup.model.vo.app;

import lombok.Data;

/**
 * @ClassName：AppSearchQueryVO
 * @Author: aoyu
 * @Date: 2025-09-18 19:40
 * @Description: 搜索应用VO
 */

@Data
public class AppSearchQueryVO {

    private String keyword;
    private Integer sortType;
    private String size;
    private String pricingModel;

}
