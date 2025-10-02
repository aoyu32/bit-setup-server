package com.aoyu.bitsetup.client.controller.community;

import com.aoyu.bitsetup.client.service.community.PostService;
import com.aoyu.bitsetup.common.result.Result;
import com.aoyu.bitsetup.model.dto.community.PostHotDTO;
import com.aoyu.bitsetup.model.dto.community.PostInfoDTO;
import com.aoyu.bitsetup.model.dto.community.PostRecommendDTO;
import com.aoyu.bitsetup.model.vo.community.PostDetailRespVO;
import com.aoyu.bitsetup.model.vo.community.PostPutReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @ClassName：PostController
 * @Author: aoyu
 * @Date: 2025-10-01 14:03
 * @Description: 社区帖子接口
 */

@Tag(name = "社区帖子")
@RestController
@RequestMapping("/api/post")
@Slf4j
public class PostController {


    @Autowired
    private PostService postService;


    @Operation(description = "发帖")
    @PostMapping("/save")
    public Result<?> savePost(@RequestBody PostPutReqVO postPutReqVO) {
        postService.savePost(postPutReqVO);
        return Result.success();
    }

    @Operation(description = "发帖图片上传")
    @PostMapping("/upload/img")
    public Result<String> uploadPostImage(MultipartFile multipartFile, String uid) {

        log.info("用户uid为：{}请求上传帖子图片{}", uid, multipartFile.toString());
        String s = postService.uploadPostImage(multipartFile, uid);
        return Result.success(s);

    }

    @Operation(description = "根据分类获取帖子列表")
    @GetMapping("/list/{category}")
    public Result<List<PostInfoDTO>> getPostList(@PathVariable String category) {

        List<PostInfoDTO> postList = postService.getPostList(category);
        return Result.success(postList);

    }


    @Operation(description = "根据帖子ID获取帖子详情")
    @GetMapping("/detail/{pid}")
    public Result<PostDetailRespVO> getPostDetail(@PathVariable String pid) {
        log.info("获取pid为{}的帖子详情", pid);
        PostDetailRespVO postDetail = postService.getPostDetail(pid);
        return Result.success(postDetail);
    }


    @Operation(description = "获取热帖")
    @GetMapping("/hot")
    public Result<List<PostHotDTO>> getHotPost() {
        log.info("获取热帖");
        List<PostHotDTO> hotPostList = postService.getHotPostList();
        return Result.success(hotPostList);
    }

    @Operation(description = "获取推荐帖子")
    @GetMapping("/recommend")
    public Result<List<PostRecommendDTO>> getRecommendPost() {
        log.info("获取推荐帖子");
        List<PostRecommendDTO> recommendPostList = postService.getRecommendPostList();
        return Result.success(recommendPostList);
    }


}
