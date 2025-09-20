package com.aoyu.bitsetup.client.mapper.app;

import com.aoyu.bitsetup.model.dto.app.AppDetailInfoDTO;
import com.aoyu.bitsetup.model.dto.app.AppGuessLikeDTO;
import com.aoyu.bitsetup.model.dto.app.AppRelatedDTO;
import com.aoyu.bitsetup.model.dto.app.AppVersionDTO;
import com.aoyu.bitsetup.model.entity.app.App;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName：AppDetailMapper
 * @Author: aoyu
 * @Date: 2025-09-17 11:23
 * @Description: 应用详情
 */

@Mapper
public interface AppDetailMapper extends BaseMapper<App> {

    /**
     * @description: 查询应用详情信息
     * @author: aoyu
     * @date: 2025/9/20 下午1:46
     * @param:
     * @return:
     */
    AppDetailInfoDTO selectAppDetailById(Long appId);

    /**
     * @description: 根据应用id查询平台信息
     * @author: aoyu
     * @date: 2025/9/20 下午1:47
     * @param:
     * @return:
     */
    List<String> selectPlatformsByAppId(Long appId);

    /**
     * @description: 根据应用id查询标签信息
     * @author: aoyu
     * @date: 2025/9/20 下午1:47
     * @param:
     * @return:
     */
    List<String> selectTagsByAppId(Long appId);

    /**
     * @description: 根据id查询截图信息
     * @author: aoyu
     * @date: 2025/9/20 下午1:47
     * @param:
     * @return:
     */
    List<String> selectScreenshotsByAppId(Long appId);

    /**
     * @description: 根据id查询应用其他版本信息
     * @author: aoyu
     * @date: 2025/9/20 下午1:47
     * @param:
     * @return:
     */
    List<AppVersionDTO> selectVersionByAppId(Long appId);

    /**
     * @description: 根据id查询相关应用列表
     * @author: aoyu
     * @date: 2025/9/20 下午1:47
     * @param:
     * @return:
     */
    List<AppRelatedDTO> selectRelated(Long appId);

    /**
     * @description: 查询猜你喜欢了列表
     * @author: aoyu
     * @date: 2025/9/20 下午1:48
     * @param:
     * @return:
     */
    List<AppGuessLikeDTO> selectGuessLike();

}
