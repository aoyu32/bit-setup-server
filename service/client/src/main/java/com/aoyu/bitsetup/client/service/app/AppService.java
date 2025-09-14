package com.aoyu.bitsetup.client.service.app;

import com.aoyu.bitsetup.model.dto.app.AppRankingListDto;

import java.util.List;

/**
 * @InterfaceName：AppService
 * @Author: aoyu
 * @Date: 2025/9/14 下午2:33
 * @Description:
 */

public interface AppService {


    /**
     * @description: 获取热门列表
     * @author: aoyu
     * @date: 2025/9/14 下午3:33
     * @param:
     * @return: 热门应用列表
     */
    List<AppRankingListDto> getHotlist();


    /**
     * @description: 获取推荐列表
     * @author: aoyu
     * @date: 2025/9/14 下午8:29
     * @param:
     * @return: 推荐应用列表
     */
    List<AppRankingListDto> getRecommendList();

    /**
     * @description: 获取必备列表
     * @author: aoyu
     * @date: 2025/9/14 下午8:29
     * @param:
     * @return: 必备应用列表
     */
    List<AppRankingListDto> getEssentialList();
}
