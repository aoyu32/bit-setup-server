package com.aoyu.bitsetup.model.vo.submit;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * @ClassName：AppRecommendSubmitReqVO
 * @Author: aoyu
 * @Date: 2025-10-05 22:08
 * @Description: 推荐应用请求VO
 */

@Data
public class AppRecommendSubmitReqVO {
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

}
