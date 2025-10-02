package com.aoyu.bitsetup.model.dto.community;

import com.aoyu.bitsetup.model.dto.user.UserBaseInfoDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName：PostInfoDTO
 * @Author: aoyu
 * @Date: 2025-10-02 09:08
 * @Description: 帖子信息DTO
 */

@Data
public class PostInfoDTO {

    private UserBaseInfoDTO user;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long pid;
    private String title;
    private String summary;
    private List<String> images;
    private String category;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    private Integer likeCount;
    private Integer commentCount;
    private Integer viewCount;
    private Integer collectCount;



}
