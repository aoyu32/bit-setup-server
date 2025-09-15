package com.aoyu.bitsetup.model.query;

import lombok.Data;

/**
 * @ClassName：PageQuery
 * @Author: aoyu
 * @Date: 2025-09-15 14:12
 * @Description: 分页查询参数实体
 */

@Data
public class PageQuery {
    private Integer pageNum;//页码
    private Integer pageSize;//每页数量
    private String sortBy;//排序字段
    private Boolean isAsc;//是否升序
}
