package com.aoyu.bitsetup.client.service.app.impl;

import com.aoyu.bitsetup.client.mapper.app.AppDetailMapper;
import com.aoyu.bitsetup.client.service.app.AppDetailService;
import com.aoyu.bitsetup.model.dto.app.AppDetailInfoDTO;
import com.aoyu.bitsetup.model.dto.app.AppGuessLikeDTO;
import com.aoyu.bitsetup.model.dto.app.AppRelatedDTO;
import com.aoyu.bitsetup.model.dto.app.AppVersionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName：AppDetailServiceImpl
 * @Author: aoyu
 * @Date: 2025-09-17 10:56
 * @Description: 应用详情服务层接口实现
 */

@Service
@Slf4j
public class AppDetailServiceImpl implements AppDetailService {

    @Autowired
    private AppDetailMapper appDetailMapper;

    @Override
    public AppDetailInfoDTO getAppDetailById(Long appId) {

        AppDetailInfoDTO appDetailInfoDTO =  appDetailMapper.selectAppDetailById(appId);
        List<String> platforms = appDetailMapper.selectPlatformsByAppId(appId);
        appDetailInfoDTO.setPlatform(platforms);
        List<String> tags = appDetailMapper.selectTagsByAppId(appId);
        appDetailInfoDTO.setTags(tags);
        List<String> screenshots = appDetailMapper.selectScreenshotsByAppId(appId);
        appDetailInfoDTO.setScreenshots(screenshots);
        List<AppVersionDTO> appVersionDTOS = appDetailMapper.selectVersionByAppId(appId);
        appDetailInfoDTO.setOtherVersions(appVersionDTOS);
        return appDetailInfoDTO;

    }

    @Override
    public List<AppRelatedDTO> getAppRelatedById(Long appId) {


        List<AppRelatedDTO> appRelatedDTOS = appDetailMapper.selectRelated(appId);
        return appRelatedDTOS;

    }

    @Override
    public List<AppGuessLikeDTO> getAppGuessLike() {
        List<AppGuessLikeDTO> appGuessLikeDTOS = appDetailMapper.selectGuessLike();
        return appGuessLikeDTOS;
    }
}
