package com.aoyu.bitsetup.model.vo.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @ClassName：CommentAddVO
 * @Author: aoyu
 * @Date: 2025-10-07 00:30
 * @Description: 发表评论请求VO
 */
@Data
@Schema(description = "发表评论请求")
public class CommentAddReqVO {

    @NotNull(message = "目标ID不能为空")
    @Schema(description = "目标ID（应用ID或帖子ID）")
    private Long targetId;

    @Size(max = 5000, message = "评论内容不能超过5000字")
    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "父评论ID，顶级评论传null或0")
    private Long parentId;

    @Schema(description = "回复的用户ID")
    private Long replyUid;

    @Schema(description = "根评论ID")
    private Long rootId;

    @Size(max = 9, message = "最多上传9张图片")
    @Schema(description = "图片URL列表")
    private List<String> imageUrls;
}