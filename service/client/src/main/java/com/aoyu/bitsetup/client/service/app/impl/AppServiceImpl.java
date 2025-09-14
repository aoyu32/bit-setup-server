package com.aoyu.bitsetup.client.service.app.impl;

import com.aoyu.bitsetup.client.mapper.app.AppMapper;
import com.aoyu.bitsetup.client.service.app.AppService;
import com.aoyu.bitsetup.model.dto.app.AppRankingListDto;
import com.aoyu.bitsetup.model.entity.app.App;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName：AppServiceImpl
 * @Author: aoyu
 * @Date: 2025-09-14 14:33
 * @Description: 应用服务接口实现类
 */

@Service
public class AppServiceImpl implements AppService {

    @Autowired
    private AppMapper appMapper;

    /**
     * @description: 获取热门应用列表
     * @author: aoyu
     * @date: 2025/9/14 下午3:47
     * @param:
     * @return: 热门应用列表
     */
    @Override
    public List<AppRankingListDto> getHotlist() {
        QueryWrapper<App> wrapper = new QueryWrapper<>();
        wrapper.eq("is_hot", 1)
                .select("id","app_name","icon_url","brief");
        List<App> hotAppList = appMapper.selectList(wrapper);
        return hotAppList.stream().map(app -> {
            AppRankingListDto appRankingListDto = new AppRankingListDto();
            BeanUtils.copyProperties(app, appRankingListDto);
            return appRankingListDto;
        }).collect(Collectors.toList());
    }

    /**
     * @description: 获取推荐列表
     * @author: aoyu
     * @date: 2025/9/14 下午8:32
     * @param:
     * @return: 推荐应用列表
     */
    @Override
    public List<AppRankingListDto> getRecommendList() {
        QueryWrapper<App> wrapper = new QueryWrapper<>();
        wrapper.eq("is_recommend", 1)
                .select("id","app_name","icon_url","brief");
        List<App> recommendList = appMapper.selectList(wrapper);
        return recommendList.stream().map(app -> {
            AppRankingListDto appRankingListDto = new AppRankingListDto();
            BeanUtils.copyProperties(app, appRankingListDto);
            return appRankingListDto;
        }).collect(Collectors.toList());
    }

    /**
     * @description: 获取必备列表
     * @author: aoyu
     * @date: 2025/9/14 下午8:32
     * @param:
     * @return: 推荐必备列表
     */
    @Override
    public List<AppRankingListDto> getEssentialList() {
        QueryWrapper<App> wrapper = new QueryWrapper<>();
        wrapper.eq("is_essential", 1)
                .select("id","app_name","icon_url","brief");
        List<App> essentialList = appMapper.selectList(wrapper);
        return essentialList.stream().map(app -> {
            AppRankingListDto appRankingListDto = new AppRankingListDto();
            BeanUtils.copyProperties(app, appRankingListDto);
            return appRankingListDto;
        }).collect(Collectors.toList());
    }

}
