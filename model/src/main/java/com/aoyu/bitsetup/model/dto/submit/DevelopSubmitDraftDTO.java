package com.aoyu.bitsetup.model.dto.submit;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @ClassName：DevelopSubmitDraftDTO
 * @Author: aoyu
 * @Date: 2025-10-05 23:46
 * @Description: 个人开发应用草稿DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DevelopSubmitDraftDTO extends SubmitDraftDTO {
    private List<String> files; // 上传文件列表
    private String iconUrl; // 图标URL
}
