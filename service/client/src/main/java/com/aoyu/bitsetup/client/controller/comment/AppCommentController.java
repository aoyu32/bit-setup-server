package com.aoyu.bitsetup.client.controller.comment;

import com.aoyu.bitsetup.client.service.comment.AppCommentService;
import com.aoyu.bitsetup.common.result.Result;
import com.aoyu.bitsetup.common.utils.ThreadLocalUtil;
import com.aoyu.bitsetup.model.vo.comment.CommentAddReqVO;
import com.aoyu.bitsetup.model.vo.comment.CommentRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @ClassName：AppCommentController
 * @Author: aoyu
 * @Date: 2025-10-07 00:23
 * @Description: 应用评论接口
 */

@Tag(name = "应用评论")
@RestController
@RequestMapping("/api/comment/app")
@Slf4j
public class AppCommentController {

    @Autowired
    private AppCommentService appCommentService;

    @PostMapping("/add")
    @Operation(summary = "发表评论")
    public Result<Long> addComment(@Valid @RequestBody CommentAddReqVO vo) {
        Long uid = (Long)ThreadLocalUtil.get("uid");
        log.info("用户{}发表应用评论，应用ID：{}", uid, vo.getTargetId());
        Long commentId = appCommentService.addComment(vo, uid);
        return Result.success(commentId);
    }

    @Operation(description = "评论图片上传")
    @PostMapping("/upload/img")
    public Result<String> uploadImage(MultipartFile file) {
        Long uid = (Long)ThreadLocalUtil.get("uid");
        log.info("用户uid为{},请求上传评论图片{}",uid,file.getOriginalFilename());
        String s = appCommentService.uploadImage(file, String.valueOf(uid));
        return  Result.success(s);
    }

    @Operation(description = "查询评论列表")
    @GetMapping("/list")
    public Result<?> getComments(@RequestParam String aid,@RequestParam String uid){
        log.info("uid为{}请求查询appId为{}的所有评论",uid,aid);
        List<CommentRespVO> comments = appCommentService.getComments(Long.valueOf(aid), Long.valueOf(uid));
        return Result.success(comments);
    }
}
