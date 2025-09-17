package com.aoyu.bitsetup.model.dto.app;

import lombok.Data;

/**
 * @ClassName：AppCategoryDTO
 * @Author: aoyu
 * @Date: 2025-09-16 13:25
 * @Description: 应用分类DTO
 */

@Data
public class AppCategoryDTO {

    private int id;
    private String categoryName;
    private String CategoryCode;
    private int sortOrder;

}
