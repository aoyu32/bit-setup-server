package com.aoyu.bitsetup.model.vo.comment;

import com.aoyu.bitsetup.model.dto.user.UserBaseInfoDTO;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName：CommentReplyRespVO
 * @Author: aoyu
 * @Date: 2025-10-07 18:44
 * @Description: 评论回复VO
 */

@Data
public class CommentReplyRespVO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cid;//评论id
    private UserBaseInfoDTO user;
    private String content;
    private List<String> images;
    private Boolean isAuthor;//是否是作者回复，应用评论默认都返回false
    private Integer likeCount;
    private Boolean isLiked;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;      // 直接回复的评论ID
    @JsonSerialize(using = ToStringSerializer.class)
    private Long replyToUid;    // 回复的用户ID
    private String replyToNickname;

    private LocalDateTime createTime;
    private LocalDateTime editTime;

}
