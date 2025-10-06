package com.aoyu.bitsetup.model.dto.submit;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * @ClassName：SubmitDrafDTO
 * @Author: aoyu
 * @Date: 2025-10-05 23:55
 * @Description: 投稿应用草稿DTO
 */

@Data
public class SubmitDraftDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id; // 投稿ID（app_submit.id）
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uid; // 用户ID
    private String appName; // 应用名称
    private String bio; // 简介
    private String downloadUrl; // 下载地址
    private String officialUrl; // 官网地址
    private Integer primaryCategory; // 一级分类ID
    private Integer secondaryCategory; // 二级分类ID
    private Integer size; // 文件大小(KB/MB)
    private String version; // 版本号
    private Boolean isDraft; // 是否草稿
    private String feeType; // 收费类型
    private List<String> screenshots; // 截图URL列表

}
