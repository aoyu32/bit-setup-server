package com.aoyu.bitsetup.client.service.user.impl;

import com.aoyu.bitsetup.client.mapper.user.UserInfoMapper;
import com.aoyu.bitsetup.client.service.user.UserInfoService;
import com.aoyu.bitsetup.common.enumeration.ResultCode;
import com.aoyu.bitsetup.common.exception.BusinessException;
import com.aoyu.bitsetup.model.dto.user.UserInfoDTO;
import com.aoyu.bitsetup.model.vo.user.UserBaseRespVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName：UserInfoServiceImpl
 * @Author: aoyu
 * @Date: 2025-09-21 15:25
 * @Description: 用户信息服务层接口
 */

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public UserBaseRespVO getBaseInfo(Long userId) {


        UserInfoDTO userInfoDTO = userInfoMapper.selectBaseInfoById(userId);
        if(userInfoDTO == null) {
            throw new BusinessException(ResultCode.USER_UN_EXIST);
        }
        UserBaseRespVO userBaseRespVO = new UserBaseRespVO();
        BeanUtils.copyProperties(userInfoDTO,userBaseRespVO);


        return null;
    }
}
