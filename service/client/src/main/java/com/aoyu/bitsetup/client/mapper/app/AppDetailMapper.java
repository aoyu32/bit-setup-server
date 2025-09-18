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

    AppDetailInfoDTO selectAppDetailById(Long appId);

    List<String> selectPlatformsByAppId(Long appId);

    List<String> selectTagsByAppId(Long appId);

    List<String> selectScreenshotsByAppId(Long appId);

    List<AppVersionDTO> selectVersionByAppId(Long appId);

    List<AppRelatedDTO> selectRelated(Long appId);

    List<AppGuessLikeDTO> selectGuessLike();

}
