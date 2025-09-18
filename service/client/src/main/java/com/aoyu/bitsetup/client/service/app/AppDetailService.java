package com.aoyu.bitsetup.client.service.app;

import com.aoyu.bitsetup.model.dto.app.AppDetailInfoDTO;
import com.aoyu.bitsetup.model.dto.app.AppGuessLikeDTO;
import com.aoyu.bitsetup.model.dto.app.AppRelatedDTO;

import java.util.List;

/**
 * @InterfaceName：AppDetailService
 * @Author: aoyu
 * @Date: 2025/9/17 上午10:56
 * @Description:
 */

public interface AppDetailService {


    public AppDetailInfoDTO getAppDetailById(Long appId);

    public List<AppRelatedDTO> getAppRelatedById(Long appId);

    List<AppGuessLikeDTO> getAppGuessLike();

}
