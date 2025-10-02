package com.aoyu.bitsetup.model.vo.community;

import lombok.Data;

import java.util.List;

/**
 * @ClassName：PostPutReqVO
 * @Author: aoyu
 * @Date: 2025-10-01 14:23
 * @Description: 发帖请求实体
 */

@Data
public class PostPutReqVO {

    private Long uid;
    private String title;
    private String summary;
    private String content;
    private String category;
    private Boolean isPublish;
    private List<String> images;

}
