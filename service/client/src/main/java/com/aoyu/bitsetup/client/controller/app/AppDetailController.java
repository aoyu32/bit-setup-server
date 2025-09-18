package com.aoyu.bitsetup.client.controller.app;

import com.aoyu.bitsetup.client.service.app.AppDetailService;
import com.aoyu.bitsetup.client.service.app.impl.AppDetailServiceImpl;
import com.aoyu.bitsetup.common.result.Result;
import com.aoyu.bitsetup.model.dto.app.AppDetailInfoDTO;
import com.aoyu.bitsetup.model.dto.app.AppGuessLikeDTO;
import com.aoyu.bitsetup.model.dto.app.AppRelatedDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName：AppDetailController
 * @Author: aoyu
 * @Date: 2025-09-17 10:53
 * @Description: 应用详情
 */

@RestController
@RequestMapping("/api/detail")
@Tag(name = "应用详情接口",description = "应用详情相关接口")
@Slf4j
public class AppDetailController {

    @Autowired
    private AppDetailService appDetailService;

    @GetMapping("/info/{appId}")
    @Operation(description = "获取应用详情，基础信息，截图，应用介绍，安装教程")
    public Result<AppDetailInfoDTO> getAppDetailInfo(@PathVariable Long appId) {
        log.info("获取应用id为：{}的应用信息",appId);
        AppDetailInfoDTO appDetailById = appDetailService.getAppDetailById(appId);
        return Result.success(appDetailById);
    }


    @GetMapping("/related/{appId}")
    @Operation(description = "获取相关推荐应用")
    public Result<List<AppRelatedDTO>> getAppRelated(@PathVariable Long appId) {

        log.info("获取相关推荐应用{}",appId);
        List<AppRelatedDTO> appRelatedById = appDetailService.getAppRelatedById(appId);
        return Result.success(appRelatedById);

    }
    @GetMapping("/guess")
    @Operation(description = "获取猜你喜欢应用")
    public Result<List<AppGuessLikeDTO>> getGuessLike() {

        log.info("获取猜你应用");
        List<AppGuessLikeDTO> appGuessLike = appDetailService.getAppGuessLike();
        return Result.success(appGuessLike);

    }



}
