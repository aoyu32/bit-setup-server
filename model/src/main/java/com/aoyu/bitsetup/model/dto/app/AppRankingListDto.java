package com.aoyu.bitsetup.model.dto.app;

import lombok.Data;

/**
 * @ClassName：AppHotListDto
 * @Author: aoyu
 * @Date: 2025-09-14 14:56
 * @Description: 热门应用列表实体
 */

@Data

public class AppRankingListDto {
    private Long id;//应用id
    private String appName;//应用名称
    private String iconUrl;//应用图片链接
    private String brief;//应用简介
}
