package com.aoyu.bitsetup.client.service.app.impl;

import com.aoyu.bitsetup.client.mapper.app.AppCategoryMapper;
import com.aoyu.bitsetup.client.service.app.AppCategoryService;
import com.aoyu.bitsetup.client.utils.AppQueryConverter;
import com.aoyu.bitsetup.common.utils.RangeStringUtil;
import com.aoyu.bitsetup.model.dto.PageResultDTO;
import com.aoyu.bitsetup.model.dto.app.AppCategoryDTO;
import com.aoyu.bitsetup.model.dto.app.AppInfoDto;
import com.aoyu.bitsetup.model.entity.app.AppCategory;
import com.aoyu.bitsetup.model.query.AppQuery;
import com.aoyu.bitsetup.model.vo.app.AppFilterQueryVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName：AppCategoryServiceImpl
 * @Author: aoyu
 * @Date: 2025-09-16 13:23
 * @Description: 应用分服务层接口实现类
 */

@Service
@Slf4j
public class AppCategoryServiceImpl implements AppCategoryService {

    @Autowired
    private AppCategoryMapper appCategoryMapper;

    /**
     * @description: 根据分类等级获取分类
     * @author: aoyu
     * @date: 2025/9/16 下午1:36
     * @param: 分类等级
     * @return: 分类列表
     */
    @Override
    public List<AppCategoryDTO> getCategoryByLevel(Integer level) {

        LambdaQueryWrapper<AppCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AppCategory::getLevel, level)
                .eq(AppCategory::getIsVisible,1)
                .orderByAsc(AppCategory::getSortOrder);

        List<AppCategory> appCategories = appCategoryMapper.selectList(queryWrapper);
        return appCategories.stream().map(appCategory -> {
            AppCategoryDTO appCategoryDTO = new AppCategoryDTO();
            BeanUtils.copyProperties(appCategory, appCategoryDTO);
            return appCategoryDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<AppCategoryDTO> getSubCategoryById(Integer id) {
        LambdaQueryWrapper<AppCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AppCategory::getParentId, id)
                .eq(AppCategory::getIsVisible,1)
                .orderByAsc(AppCategory::getSortOrder);
        List<AppCategory> appCategories = appCategoryMapper.selectList(queryWrapper);
        return appCategories.stream().map(appCategory -> {
            AppCategoryDTO appCategoryDTO = new AppCategoryDTO();
            BeanUtils.copyProperties(appCategory, appCategoryDTO);
            return appCategoryDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public PageResultDTO<AppInfoDto> getAppByFilter(AppFilterQueryVO appFilterQueryVO) {
        AppQuery appQuery = AppQueryConverter.toAppQuery(appFilterQueryVO);

        Page<AppInfoDto> page = new Page<>(appQuery.getPageNum(), appQuery.getPageSize());
        log.info("分页条件：{}", appQuery);

        IPage<AppInfoDto> appInfoDtoIPage = appCategoryMapper.selectAppByFilter(page, appQuery);

        log.info("分页结果：总页数：{}，每页条数：{}，总条数：{}，记录：{}",
                appInfoDtoIPage.getPages(),
                appInfoDtoIPage.getSize(),
                appInfoDtoIPage.getTotal(),
                appInfoDtoIPage.getRecords());

        return new PageResultDTO<>(
                appInfoDtoIPage.getTotal(),
                appInfoDtoIPage.getPages(),
                appQuery.getPageNum(),
                appQuery.getPageSize(),
                appInfoDtoIPage.getRecords()
        );
    }






}
