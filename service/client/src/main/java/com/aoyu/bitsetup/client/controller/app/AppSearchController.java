package com.aoyu.bitsetup.client.controller.app;

import com.aoyu.bitsetup.client.service.app.AppSearchService;
import com.aoyu.bitsetup.common.result.Result;
import com.aoyu.bitsetup.common.utils.ThreadLocalUtil;
import com.aoyu.bitsetup.model.dto.PageResultDTO;
import com.aoyu.bitsetup.model.dto.app.AppInfoDto;
import com.aoyu.bitsetup.model.dto.user.UserSearchHistoryDTO;
import com.aoyu.bitsetup.model.vo.app.AppSearchQueryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        log.info("根据搜索关键字{}获取搜索提示", keyword);
        List<String> searchTips = appSearchService.getSearchTips(keyword);
        return Result.success(searchTips);
    }

    @Operation(description = "条件搜索应用")
    @PostMapping("/page")
    public Result<PageResultDTO<AppInfoDto>> searchApp(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestBody AppSearchQueryVO appSearchQueryVO) {
        log.info("条件搜索查询条件：{},{},{}", pageNum, pageSize, appSearchQueryVO);
        PageResultDTO<AppInfoDto> appInfoDtoPageResultDTO = appSearchService.searchApp(pageNum, pageSize, appSearchQueryVO);
        return Result.success(appInfoDtoPageResultDTO);
    }

    @Operation(description = "获取搜索历史")
    @GetMapping("/history")
    public Result<List<UserSearchHistoryDTO>> getSearchHistory() {

        log.info("用户uid为：{}请求获取所有搜索历史", ThreadLocalUtil.get("uid"));
        List<UserSearchHistoryDTO> historyList = appSearchService.getSearchHistory((Long) ThreadLocalUtil.get("uid"));
        return Result.success(historyList);

    }

    @Operation(description = "删除搜索历史记录")
    @PostMapping("/delete")
    public Result<?> deleteHistoryItem(@RequestBody Map<String, Object> params) {
        Long uid = (Long) ThreadLocalUtil.get("uid");
        Object sidObj = params.get("sid");
        if (sidObj == null) {
            return Result.error(400,"参数 sid 不能为空");
        }
        String sid = sidObj.toString();
        log.info("用户uid={}请求删除历史搜索记录id={}", uid, sid);
        log.info("用户uid={}请求删除历史搜索记录id={}", uid, sid);
        appSearchService.deleteSearchHistory(uid, sid);
        return Result.success("删除成功");
    }

    @Operation(description = "保存搜索历史记录")
    @PostMapping("/save/history")
    public Result<?> saveHistory(@RequestBody Map<String, Object> params) {
        Long uid = (Long) ThreadLocalUtil.get("uid");
        String keyword = (String) params.get("keyword");
        log.info("用户uid={}请求保存搜索历史keyword={}", uid, keyword);
        appSearchService.saveSearchHistory(uid, keyword);
        return Result.success("保存成功");

    }

}
