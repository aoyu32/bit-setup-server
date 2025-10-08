package com.aoyu.bitsetup.model.vo.comment;

import com.aoyu.bitsetup.model.dto.user.UserBaseInfoDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName：CommentRespVO
 * @Author: aoyu
 * @Date: 2025-10-07 18:43
 * @Description: 评论响应VO
 */

@Data
public class CommentRespVO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cid;//评论id
    private UserBaseInfoDTO user;
    private Boolean isAuthor;//是否是作者回复，应用评论默认都返回false
    private String content;
    private List<String> images;

    private Integer likeCount;
    private Boolean isLiked;
    private Integer replyCount;

    private Boolean isTop;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private LocalDateTime editTime;

    // 所有回复列表
    private List<CommentReplyRespVO> replies;
}
