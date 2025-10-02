package com.aoyu.bitsetup.model.dto.community;

import com.aoyu.bitsetup.model.dto.user.UserBaseInfoDTO;
import com.aoyu.bitsetup.model.dto.user.UserDetailInfoDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName：PostDetailDTO
 * @Author: aoyu
 * @Date: 2025-10-02 14:15
 * @Description: 帖子详情DTO
 */

@Data
public class PostDetailDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long pid;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uid;
    private String title;
    private String summary;
    private String content;
    private List<String> images;
    private String category;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    private Integer likeCount;
    private Integer commentCount;
    private Integer viewCount;
    private Integer collectCount;


}
