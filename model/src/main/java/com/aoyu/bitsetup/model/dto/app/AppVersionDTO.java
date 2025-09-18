package com.aoyu.bitsetup.model.dto.app;

import lombok.Data;

/**
 * @ClassName：AppVersionDTO
 * @Author: aoyu
 * @Date: 2025-09-17 23:26
 * @Description: 应用版本DTO
 */

@Data
public class AppVersionDTO {

    private Long id;
    private String version;
    private String downloadUrl;
    private String releaseNotes;
    private Integer size;
    private String downloadCount;

}
