package com.aoyu.bitsetup.model.dto.app;

import lombok.Data;

/**
 * @ClassName：AppGuessLikeDTO
 * @Author: aoyu
 * @Date: 2025-09-18 08:24
 * @Description: 猜你喜欢DTO
 */

@Data
public class AppGuessLikeDTO {

    private Long appId;
    private String appName;
    private String iconUrl;
    private String category;


}
