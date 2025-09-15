package com.aoyu.bitsetup.client.controller.app;

import com.aoyu.bitsetup.client.service.app.AppService;
import com.aoyu.bitsetup.common.result.Result;
import com.aoyu.bitsetup.model.dto.PageResultDTO;
import com.aoyu.bitsetup.model.dto.app.AppInfoDto;
import com.aoyu.bitsetup.model.dto.app.AppRankingListDto;
import com.aoyu.bitsetup.model.query.AppQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName：AppController
 * @Author: aoyu
 * @Date: 2025-09-14 14:28
 * @Description: 应用接口
 */
@Slf4j
@RestController
@RequestMapping("/api/app")
@Tag(name = "应用接口",description = "应用相关接口，包括获取应用列表，热门应用，推荐应用，必备应用等")
public class AppController {

    @Autowired
    private AppService appService;

    /**
     * @description: 获取热门应用列表
     * @author: aoyu
     * @date: 2025/9/14 下午3:17
     * @param:
     * @return: 热门应用列表
     */
    @Operation(description = "热门应用列表")
    @GetMapping("/hot")
    public Result<List<AppRankingListDto>> getHotList(){
        List<AppRankingListDto> hotlist = appService.getHotlist();
        return Result.success(hotlist);
    }

    /**
     * @description: 获取推荐应用列表
     * @author: aoyu
     * @date: 2025/9/14 下午8:33
     * @param:
     * @return: 推荐应用列表
     */
    @Operation(description = "推荐应用列表")
    @GetMapping("/recommend")
    public Result<List<AppRankingListDto>> getRecommendList(){
        List<AppRankingListDto> recommendList = appService.getRecommendList();
        return Result.success(recommendList);
    }


    /**
     * @description: 获取必备应用列表
     * @author: aoyu
     * @date: 2025/9/14 下午8:33
     * @param:
     * @return: 必备应用列表
     */
    @Operation(description = "必备应用列表")
    @GetMapping("/must")
    public Result<List<AppRankingListDto>> getEssentialList(){
        List<AppRankingListDto> essentialList = appService.getEssentialList();
        return Result.success(essentialList);
    }


    /**
     * @description: 分页获取应用列表
     * @author: aoyu
     * @date: 2025/9/15 下午11:25
     * @param: 页码，每页条数
     * @return: 分页应用列表
     */
    @Operation(description = "分页获取应用列表")
    @GetMapping("/page")
    public Result<PageResultDTO<AppInfoDto>> getAppInfoPage(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        AppQuery appQuery = new AppQuery();
        appQuery.setPageNum(pageNum);
        appQuery.setPageSize(pageSize);
        PageResultDTO<AppInfoDto> appInfoPage = appService.getAppInfoPage(appQuery);
        return Result.success(appInfoPage);
    }





}
