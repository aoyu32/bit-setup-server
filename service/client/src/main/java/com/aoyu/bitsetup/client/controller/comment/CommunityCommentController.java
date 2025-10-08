package com.aoyu.bitsetup.client.controller.comment;

import com.aoyu.bitsetup.client.service.comment.CommunityCommentService;
import com.aoyu.bitsetup.common.result.Result;
import com.aoyu.bitsetup.common.utils.ThreadLocalUtil;
import com.aoyu.bitsetup.model.vo.comment.CommentAddReqVO;
import com.aoyu.bitsetup.model.vo.comment.CommentRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @ClassName：PostCommentController
 * @Author: aoyu
 * @Date: 2025-10-07 00:24
 * @Description: 帖子评论
 */
@Tag(name = "社区评论")
@RestController
@RequestMapping("/api/comment/community")
@Slf4j
public class CommunityCommentController {
    @Autowired
    private CommunityCommentService communityCommentService;

    @PostMapping("/add")
    @Operation(summary = "发表评论")
    public Result<Long> addComment(@Valid @RequestBody CommentAddReqVO vo) {
        Long uid = (Long) ThreadLocalUtil.get("uid");
        log.info("用户{}发表社区评论，帖子ID：{}", uid, vo.getTargetId());
        Long commentId = communityCommentService.addComment(vo, uid);
        return Result.success(commentId);
    }

    @Operation(description = "评论图片上传")
    @PostMapping("/upload/img")
    public Result<String> uploadImage(MultipartFile file) {
        Long uid = (Long) ThreadLocalUtil.get("uid");
        log.info("用户uid为{},请求上传评论图片{}", uid, file.getOriginalFilename());
        String imageUrl = communityCommentService.uploadImage(file, String.valueOf(uid));
        return Result.success(imageUrl);
    }

    @Operation(description = "查询评论列表")
    @GetMapping("/list")
    public Result<List<CommentRespVO>> getComments(@RequestParam String postId, @RequestParam String uid) {
        log.info("uid为{}请求查询postId为{}的所有评论", uid, postId);
        List<CommentRespVO> comments = communityCommentService.getComments(Long.valueOf(postId), Long.valueOf(uid));
        return Result.success(comments);
    }

    @Operation(description = "删除评论")
    @DeleteMapping("/{commentId}")
    public Result<Void> deleteComment(@PathVariable Long commentId) {
        Long uid = (Long) ThreadLocalUtil.get("uid");
        log.info("用户{}请求删除评论{}", uid, commentId);
        communityCommentService.deleteComment(commentId, uid);
        return Result.success();
    }
}
