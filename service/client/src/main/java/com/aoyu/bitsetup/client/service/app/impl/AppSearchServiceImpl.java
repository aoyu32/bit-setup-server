package com.aoyu.bitsetup.client.service.app.impl;

import com.aoyu.bitsetup.client.mapper.app.AppCategoryMapper;
import com.aoyu.bitsetup.client.mapper.app.AppDetailMapper;
import com.aoyu.bitsetup.client.mapper.app.AppSearchMapper;
import com.aoyu.bitsetup.client.service.app.AppSearchService;
import com.aoyu.bitsetup.common.enumeration.SortTypeEnum;
import com.aoyu.bitsetup.common.utils.RangeStringUtil;
import com.aoyu.bitsetup.model.dto.PageResultDTO;
import com.aoyu.bitsetup.model.dto.app.AppInfoDto;
import com.aoyu.bitsetup.model.entity.app.App;
import com.aoyu.bitsetup.model.entity.app.AppCategory;
import com.aoyu.bitsetup.model.query.SearchQuery;
import com.aoyu.bitsetup.model.vo.app.AppSearchQueryVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName：AppSearchServiceImpl
 * @Author: aoyu
 * @Date: 2025-09-18 15:21
 * @Description: 应用搜索服务层接口实现类
 */

@Service
public class AppSearchServiceImpl implements AppSearchService {

    @Autowired
    private AppSearchMapper appSearchMapper;

    @Autowired
    private AppCategoryMapper appCategoryMapper;



    @Override
    public List<String> getSearchTips(String keyword) {
        // 创建查询条件
        QueryWrapper<App> queryWrapper = new QueryWrapper<>();
        QueryWrapper<AppCategory> appCategoryQueryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT app_name")  // 去重查询app_name字段
                .like("app_name", keyword)
                .eq("status", 1)
                .orderByAsc("app_name")
                .last("LIMIT 5");        // 按名称排序

        appCategoryQueryWrapper.select("DISTINCT category_name")
                .like("category_name", keyword)
                .eq("is_visible",1)
                .orderByAsc("category_name")
                .last("LIMIT 5");


        // 执行查询，直接获取字符串列表
        List<Object> appNames = appSearchMapper.selectObjs(queryWrapper);
        List<Object> appCategories = appCategoryMapper.selectObjs(appCategoryQueryWrapper);

        // 转换为字符串列表
        List<String> appNamesCollect = appNames.stream()
                .map(obj -> (String) obj)
                .collect(Collectors.toList());
        List<String> appCategoryCollect = appCategories.stream().map(obj -> (String) obj).collect(Collectors.toList());

        return Stream.concat(appNamesCollect.stream(), appCategoryCollect.stream())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public PageResultDTO<AppInfoDto> searchApp(Integer pageNum,Integer pageSize,AppSearchQueryVO appSearchQueryVO) {

        SearchQuery searchQuery = new SearchQuery();
        //VO转Query
        searchQuery.setKeyword(appSearchQueryVO.getKeyword());

        if(appSearchQueryVO.getSortType() != null){
            String column = SortTypeEnum.fromCode(appSearchQueryVO.getSortType()).getColumn();
            searchQuery.setSortBy(column);
        }

        Map<String, Long> minAndMax = RangeStringUtil.getMinAndMax(appSearchQueryVO.getSize(), Long.class);
        searchQuery.setMinSize(minAndMax.get("min"));
        searchQuery.setMaxSize(minAndMax.get("max"));
        searchQuery.setPricingModel(appSearchQueryVO.getPricingModel());

        Page<AppInfoDto> page = new Page<>(pageNum, pageSize);
        IPage<AppInfoDto> appInfoDtoIPage = appSearchMapper.selectAppBySearch(page, searchQuery);
        PageResultDTO<AppInfoDto> appInfoDtoPageResultDTO = new PageResultDTO<>();
        appInfoDtoPageResultDTO.setPageNum(pageNum);
        appInfoDtoPageResultDTO.setPageSize(pageSize);
        appInfoDtoPageResultDTO.setTotal(appInfoDtoIPage.getTotal());
        appInfoDtoPageResultDTO.setList(appInfoDtoIPage.getRecords());
        appInfoDtoPageResultDTO.setPages(appInfoDtoIPage.getPages());

        return appInfoDtoPageResultDTO;
    }


}
