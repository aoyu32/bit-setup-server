package com.aoyu.bitsetup.client.controller.app;

import com.aoyu.bitsetup.client.service.app.AppSearchService;
import com.aoyu.bitsetup.common.result.Result;
import com.aoyu.bitsetup.model.dto.PageResultDTO;
import com.aoyu.bitsetup.model.dto.app.AppInfoDto;
import com.aoyu.bitsetup.model.query.SearchQuery;
import com.aoyu.bitsetup.model.vo.app.AppSearchQueryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName：AppSearchController
 * @Author: aoyu
 * @Date: 2025-09-18 14:48
 * @Description: 应用搜索
 */

@Tag(name = "应用搜索", description = "应用搜索相关接口")
@RestController
@RequestMapping("/api/search")
@Slf4j
public class AppSearchController {

    @Autowired
    private AppSearchService appSearchService;

    @Operation(description = "获取搜索提示")
    @GetMapping("/tips")
    public Result<List<String>> getSearchTips(@RequestParam String keyword) {
        log.info("根据搜索关键字{}获取搜索提示",keyword);
        List<String> searchTips = appSearchService.getSearchTips(keyword);
        return Result.success(searchTips);
    }

    @Operation(description = "条件搜索应用")
    @PostMapping("/page")
    public Result<PageResultDTO<AppInfoDto>> searchApp(@RequestParam Integer pageNum, @RequestParam Integer pageSize,@RequestBody AppSearchQueryVO appSearchQueryVO){
        log.info("条件搜索查询条件：{},{},{}",pageNum,pageSize,appSearchQueryVO);
        PageResultDTO<AppInfoDto> appInfoDtoPageResultDTO = appSearchService.searchApp(pageNum, pageSize,appSearchQueryVO);
        return Result.success(appInfoDtoPageResultDTO);
    }


}
