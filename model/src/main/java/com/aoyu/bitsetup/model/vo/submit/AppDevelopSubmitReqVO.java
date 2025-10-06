package com.aoyu.bitsetup.model.vo.submit;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName：AppDevelopSubmitReqVO
 * @Author: aoyu
 * @Date: 2025-10-05 23:13
 * @Description: 个人开发应用提交请求VO
 */

@Data
public class AppDevelopSubmitReqVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private String appName;
    private String bio;
    private String downloadUrl;
    private String officialUrl;
    private Integer primaryCategory;
    private Integer secondaryCategory;
    private Integer size;
    private String version;
    private List<String> screenshots;
    private Boolean isDraft;
    private String feeType;
    private String iconUrl;
    private String fileUrl;
    private List<String> proveImages;

}
