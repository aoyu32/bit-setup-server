package com.aoyu.bitsetup.client.controller.submit;

import com.aoyu.bitsetup.client.service.submit.AppSubmitService;
import com.aoyu.bitsetup.common.result.Result;
import com.aoyu.bitsetup.common.utils.ThreadLocalUtil;
import com.aoyu.bitsetup.model.dto.submit.SubmitDraftDTO;
import com.aoyu.bitsetup.model.vo.submit.AppDevelopSubmitReqVO;
import com.aoyu.bitsetup.model.vo.submit.AppRecommendSubmitReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName：AppSubmitController
 * @Author: aoyu
 * @Date: 2025-10-05 13:36
 * @Description: 应用投稿
 */

@Tag(name = "应用投稿")
@RequestMapping("/api/submit")
@RestController
@Slf4j
public class AppSubmitController {

    @Autowired
    private AppSubmitService appSubmitService;

    @Operation(description = "提交推荐应用")
    @PostMapping("/recommend")
    public Result<?> submitRecommend(@RequestBody AppRecommendSubmitReqVO appRecommendSubmitReqVO){
        log.info("用户uid为：{}推荐了应用：{}",ThreadLocalUtil.get("uid"),appRecommendSubmitReqVO);
        appSubmitService.submitRecommend(appRecommendSubmitReqVO);
        return Result.success();
    }

    @Operation(description = "提交个人开发应用")
    @PostMapping("/develop")
    public Result<?> submitDevelop(@RequestBody AppDevelopSubmitReqVO appDevelopSubmitReqVO){
        log.info("用户uid为：{}上传了个人开发了应用：{}",ThreadLocalUtil.get("uid"),appDevelopSubmitReqVO);
        appSubmitService.submitDevelop(appDevelopSubmitReqVO);
        return Result.success();
    }

    @Operation(description = "上传截图")
    @PostMapping("/upload/img")
    public Result<String> imgUpload(MultipartFile file){
        log.info("uid为{}上传截图文件{}",ThreadLocalUtil.get("uid"),file.getOriginalFilename());
        String url = appSubmitService.uploadImg(file,String.valueOf(ThreadLocalUtil.get("uid")));
        return Result.success(url);
    }

    @Operation(description = "获取暂存投稿应用表单")
    @GetMapping("/draft")
    public Result<SubmitDraftDTO> getSubmitDraft(String type){
       log.info("用户uid为：{}请求获取类型为{}的投稿应用表单数据",type,ThreadLocalUtil.get("uid"));
        SubmitDraftDTO submitDraft = appSubmitService.getSubmitDraft(type, String.valueOf(ThreadLocalUtil.get("uid")));
        return Result.success(submitDraft);
    }

    @Operation(description = "上传投稿应用文件")
    @PostMapping("/file")
    public Result<?> fileUpload(MultipartFile file){
        log.info("用户uid为欸：{}请求上传投稿应用文件{}", ThreadLocalUtil.get("uid"), file);
        String s = appSubmitService.uploadFile(file,String.valueOf(ThreadLocalUtil.get("uid")));
        return Result.success(s);

    }


}
