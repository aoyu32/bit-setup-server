package com.aoyu.bitsetup.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName：PageDTO
 * @Author: aoyu
 * @Date: 2025-09-15 14:16
 * @Description: 通用分页结果DTO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResultDTO<T> {
    private Long total;//总条数
    private Long pages;//总页数
    private int pageNum;//页码
    private int pageSize;//每页条数
    private List<T> list;//数据集合
}
