package com.aoyu.bitsetup.client.service.app.impl;

import com.aoyu.bitsetup.client.mapper.app.AppCategoryMapper;
import com.aoyu.bitsetup.client.mapper.app.AppSearchMapper;
import com.aoyu.bitsetup.client.mapper.user.UserSearchHistoryMapper;
import com.aoyu.bitsetup.client.service.app.AppSearchService;
import com.aoyu.bitsetup.common.enumeration.SortTypeEnum;
import com.aoyu.bitsetup.common.utils.RangeStringUtil;
import com.aoyu.bitsetup.model.dto.PageResultDTO;
import com.aoyu.bitsetup.model.dto.app.AppInfoDto;
import com.aoyu.bitsetup.model.dto.user.UserSearchHistoryDTO;
import com.aoyu.bitsetup.model.entity.app.App;
import com.aoyu.bitsetup.model.entity.app.AppCategory;
import com.aoyu.bitsetup.model.entity.user.UserSearchHistory;
import com.aoyu.bitsetup.model.query.SearchQuery;
import com.aoyu.bitsetup.model.vo.app.AppSearchQueryVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@Slf4j
public class AppSearchServiceImpl implements AppSearchService {

    @Autowired
    private AppSearchMapper appSearchMapper;

    @Autowired
    private AppCategoryMapper appCategoryMapper;

    @Autowired
    private UserSearchHistoryMapper userSearchHistoryMapper;


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
                .eq("is_visible", 1)
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
    public PageResultDTO<AppInfoDto> searchApp(Integer pageNum, Integer pageSize, AppSearchQueryVO appSearchQueryVO) {

        SearchQuery searchQuery = new SearchQuery();
        //VO转Query
        searchQuery.setKeyword(appSearchQueryVO.getKeyword());

        if (appSearchQueryVO.getSortType() != null) {
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

    @Override
    public void saveSearchHistory(Long uid, String keyword) {
        // 根据uid和keyword查询是否已存在该历史记录
        LambdaQueryWrapper<UserSearchHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserSearchHistory::getUid, uid)
                .eq(UserSearchHistory::getKeyword, keyword)
                .eq(UserSearchHistory::getIsDeleted, 0);
        UserSearchHistory existingHistory = userSearchHistoryMapper.selectOne(queryWrapper);

        if (existingHistory != null) {
            // 删除现有记录（逻辑删除或物理删除）
            userSearchHistoryMapper.deleteById(existingHistory.getId());
        }

        // 插入新记录，create_time会自动设置为当前时间
        UserSearchHistory newHistory = new UserSearchHistory();
        newHistory.setUid(uid);
        newHistory.setKeyword(keyword);
        newHistory.setSearchType("general");
        newHistory.setSearchSource("web");
        userSearchHistoryMapper.insert(newHistory);
    }

    @Override
    public List<UserSearchHistoryDTO> getSearchHistory(Long uid) {
        LambdaQueryWrapper<UserSearchHistory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserSearchHistory::getUid, uid)
                .eq(UserSearchHistory::getIsDeleted, 0)
                .orderByDesc(UserSearchHistory::getCreateTime);
        List<UserSearchHistory> historyList = userSearchHistoryMapper.selectList(lambdaQueryWrapper);
        return historyList.stream().map(history -> {
            UserSearchHistoryDTO userSearchHistoryDTO = new UserSearchHistoryDTO();
            userSearchHistoryDTO.setId(history.getId());
            userSearchHistoryDTO.setKeyword(history.getKeyword());
            return userSearchHistoryDTO;
        }).collect(Collectors.toList());

    }

    @Override
    public void deleteSearchHistory(Long uid, String sid) {
        if (StringUtils.isNotBlank(sid)) {
            // 根据ID删除单条记录
            deleteSingleHistory(uid, sid);
        } else {
            // 删除该用户的全部历史记录
            deleteAllHistory(uid);
        }
    }

    /**
     * 删除单条搜索历史记录
     */
    private void deleteSingleHistory(Long uid, String sid) {

        Long historyId = Long.parseLong(sid);
        LambdaQueryWrapper<UserSearchHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserSearchHistory::getId, historyId)
                .eq(UserSearchHistory::getUid, uid)
                .eq(UserSearchHistory::getIsDeleted, 0);

        UserSearchHistory history = userSearchHistoryMapper.selectOne(queryWrapper);
        if (history != null) {
            history.setIsDeleted(1);
            userSearchHistoryMapper.updateById(history);
            log.info("用户{}成功删除搜索历史记录ID: {}", uid, historyId);
        } else {
            log.warn("用户{}尝试删除不存在的搜索历史记录ID: {}", uid, historyId);
        }

    }

    /**
     * 删除用户的全部搜索历史记录
     */
    private void deleteAllHistory(Long uid) {
        LambdaUpdateWrapper<UserSearchHistory> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserSearchHistory::getUid, uid)
                .eq(UserSearchHistory::getIsDeleted, 0)
                .set(UserSearchHistory::getIsDeleted, 1);

        int deletedCount = userSearchHistoryMapper.update(null, updateWrapper);
        log.info("用户{}成功删除全部搜索历史记录，共{}条", uid, deletedCount);
    }
}
