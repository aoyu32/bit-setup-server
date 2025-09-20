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


    /**
     * @description: 根据id获取应用信息
     * @author: aoyu
     * @date: 2025/9/20 下午1:50
     * @param:
     * @return:
     */
    AppDetailInfoDTO getAppDetailById(Long appId);

    /**
     * @description: 根据id获取相关应用列表
     * @author: aoyu
     * @date: 2025/9/20 下午1:51
     * @param:
     * @return:
     */
    List<AppRelatedDTO> getAppRelatedById(Long appId);

    /**
     * @description: 获取猜你喜欢应用列表
     * @author: aoyu
     * @date: 2025/9/20 下午1:53
     * @param:
     * @return:
     */
    List<AppGuessLikeDTO> getAppGuessLike();

}
