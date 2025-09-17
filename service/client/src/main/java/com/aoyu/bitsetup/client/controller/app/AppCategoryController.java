package com.aoyu.bitsetup.client.controller.app;

import com.aoyu.bitsetup.client.service.app.AppCategoryService;
import com.aoyu.bitsetup.common.result.Result;
import com.aoyu.bitsetup.model.dto.PageResultDTO;
import com.aoyu.bitsetup.model.dto.app.AppCategoryDTO;
import com.aoyu.bitsetup.model.dto.app.AppInfoDto;
import com.aoyu.bitsetup.model.vo.app.AppFilterQueryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName：AppCategoryController
 * @Author: aoyu
 * @Date: 2025-09-16 13:14
 * @Description: 应用分类
 */

@Tag(name = "应用分类", description = "应用分类相关接口")
@RestController
@RequestMapping("/api/category")
@Slf4j
public class AppCategoryController {

    @Autowired
    private AppCategoryService appCategoryService;

    @Operation(description = "根据分类等级获取分类")
    @GetMapping("/level/{level}")
    public Result<List<AppCategoryDTO>> getCategoryByLevel(@PathVariable int level) {
       log.info("获取{}级分类：",level);
        List<AppCategoryDTO> categoryDTOList = appCategoryService.getCategoryByLevel(level);
        return Result.success(categoryDTOList);
    }

    @Operation(description = "根据分类id获取子分类")
    @GetMapping("/sub")
    public Result<List<AppCategoryDTO>> getSubCategoryById(@RequestParam int categoryId) {
        log.info("获取分类id为{}的所有子分类：",categoryId);
        List<AppCategoryDTO> categoryDTOList = appCategoryService.getSubCategoryById(categoryId);
        return Result.success(categoryDTOList);
    }

    @Operation(description = "根据分类和过滤条件查询应用")
    @PostMapping("/filter")
    public Result<PageResultDTO<AppInfoDto>> getAppByFilter(@RequestBody AppFilterQueryVO appFilterQueryVO){
        log.info("过滤查询APP{},{}",appFilterQueryVO,"");
        PageResultDTO<AppInfoDto> appByFilter = appCategoryService.getAppByFilter(appFilterQueryVO);
        return  Result.success(appByFilter);
    }
}
