package com.aoyu.bitsetup.client.mapper.app;

import com.aoyu.bitsetup.model.dto.app.AppInfoDto;
import com.aoyu.bitsetup.model.entity.app.App;
import com.aoyu.bitsetup.model.query.SearchQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName：AppSearchMapper
 * @Author: aoyu
 * @Date: 2025-09-18 14:58
 * @Description: 应用搜索
 */

public interface AppSearchMapper extends BaseMapper<App> {

    /**
     * @description: 根据查询条件分页搜索应用
     * @author: aoyu
     * @date: 2025/9/20 下午1:53
     * @param:
     * @return:
     */
    IPage<AppInfoDto> selectAppBySearch(Page<AppInfoDto> page, @Param("search") SearchQuery searchQuery);

}
