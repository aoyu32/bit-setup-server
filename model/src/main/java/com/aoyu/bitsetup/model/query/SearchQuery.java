package com.aoyu.bitsetup.model.query;

import lombok.Data;

/**
 * @ClassName：SearchQuery
 * @Author: aoyu
 * @Date: 2025-09-18 19:31
 * @Description: 搜索查询
 */

@Data
public class SearchQuery extends PageQuery{

    private String keyword;
    private Long maxSize;
    private Long minSize;
    private String pricingModel;

}
