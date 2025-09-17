package com.aoyu.bitsetup.client.mapper.app;

import com.aoyu.bitsetup.model.dto.app.AppInfoDto;
import com.aoyu.bitsetup.model.entity.app.AppCategory;
import com.aoyu.bitsetup.model.query.AppQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @InterfaceName：AppCategoryMapper
 * @Author: aoyu
 * @Date: 2025/9/16 下午1:22
 * @Description:
 */

public interface AppCategoryMapper extends BaseMapper<AppCategory> {
    IPage<AppInfoDto> selectAppByFilter(Page<AppInfoDto> pag,@Param("query") AppQuery query);
}
