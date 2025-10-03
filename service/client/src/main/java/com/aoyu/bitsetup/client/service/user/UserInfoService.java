package com.aoyu.bitsetup.client.service.user;

import com.aoyu.bitsetup.model.dto.user.UserInfoDTO;
import com.aoyu.bitsetup.model.vo.user.UserBaseRespVO;
import com.aoyu.bitsetup.model.vo.user.UserUpdateReqVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @InterfaceName：UserInfoService
 * @Author: aoyu
 * @Date: 2025/9/21 下午3:25
 * @Description:
 */

public interface UserInfoService {


    /**
     * @description: 获取用户基本信息
     * @author: aoyu
     * @date: 2025/9/21 下午9:53
     * @param:
     * @return:
     */
   UserInfoDTO getBaseInfo(Long uid);

   void updateBaseInfo(UserUpdateReqVO userUpdateReqVO);

   String uploadAvatar(MultipartFile file,String uid);


}
