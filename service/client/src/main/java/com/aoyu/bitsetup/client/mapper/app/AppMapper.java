package com.aoyu.bitsetup.client.mapper.app;

import com.aoyu.bitsetup.model.dto.app.AppInfoDto;
import com.aoyu.bitsetup.model.entity.app.App;
import com.aoyu.bitsetup.model.query.AppQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @InterfaceName：AppMapper
 * @Author: aoyu
 * @Date: 2025/9/14 下午2:29
 * @Description:
 */

@Mapper
public interface AppMapper extends BaseMapper<App> {

    /**
     * @description: 分页查询应用信息
     * @author: aoyu
     * @date: 2025/9/15 下午2:25
     * @param: 分页条件，查询条件
     * @return: 应用信息分页数据
     */
    IPage<AppInfoDto> selectAppInfoPage(Page<AppInfoDto> page);


}
