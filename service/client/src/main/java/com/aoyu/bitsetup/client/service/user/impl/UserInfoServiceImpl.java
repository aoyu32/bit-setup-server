package com.aoyu.bitsetup.client.service.user.impl;

import com.aoyu.bitsetup.client.mapper.user.UserInfoMapper;
import com.aoyu.bitsetup.client.service.user.UserInfoService;
import com.aoyu.bitsetup.common.enumeration.ResultCode;
import com.aoyu.bitsetup.common.exception.BusinessException;
import com.aoyu.bitsetup.common.utils.MinioUtil;
import com.aoyu.bitsetup.model.dto.user.UserBaseInfoDTO;
import com.aoyu.bitsetup.model.dto.user.UserInfoDTO;
import com.aoyu.bitsetup.model.entity.user.UserInfo;
import com.aoyu.bitsetup.model.vo.user.UserBaseRespVO;
import com.aoyu.bitsetup.model.vo.user.UserUpdateReqVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Autowired
    private MinioUtil minioUtil;

    @Override
    public UserInfoDTO getBaseInfo(Long userId) {


        UserInfoDTO userInfoDTO = userInfoMapper.selectBaseInfoById(userId);
        if(userInfoDTO == null) {
            throw new BusinessException(ResultCode.USER_UN_EXIST);
        }
//        UserBaseRespVO userBaseRespVO = new UserBaseRespVO();
//        BeanUtils.copyProperties(userInfoDTO,userBaseRespVO);

        return userInfoDTO;
    }

    @Override
    public void updateBaseInfo(UserUpdateReqVO updateVO) {
        LambdaUpdateWrapper<UserInfo> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(UserInfo::getUid, Long.valueOf(updateVO.getUid()));

        // 设置需要更新的字段
        if (updateVO.getAvatar() != null) {
            updateWrapper.set(UserInfo::getAvatar, updateVO.getAvatar());
        }
        if (updateVO.getNickname() != null) {
            updateWrapper.set(UserInfo::getNickname, updateVO.getNickname());
        }
        if (updateVO.getBio() != null) {
            updateWrapper.set(UserInfo::getBio, updateVO.getBio());
        }
        if (updateVO.getGender() != null) {
            updateWrapper.set(UserInfo::getGender, updateVO.getGender());
        }
        if (updateVO.getCity() != null) {
            updateWrapper.set(UserInfo::getCity, updateVO.getCity());
        }
        if (updateVO.getProvince() != null) {
            updateWrapper.set(UserInfo::getProvince, updateVO.getProvince());
        }
        if (updateVO.getCareer() != null) {
            updateWrapper.set(UserInfo::getCareer, updateVO.getCareer());
        }

        int res = userInfoMapper.update(null, updateWrapper);

        if(res == 0){
            throw new BusinessException(ResultCode.USER_INFO_UPDATE_ERROR);
        }


    }

    @Override
    public String uploadAvatar(MultipartFile file, String uid) {
        String path = "user/avatar/" + uid;
        return minioUtil.uploadFileToFolder(file, path, true);
    }

    @Override
    public Map<Long, UserBaseInfoDTO> getUserBaseInfoMap(Set<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return new HashMap<>();
        }

        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UserInfo::getUid, userIds);

        List<UserInfo> userInfos = userInfoMapper.selectList(wrapper);

        return userInfos.stream()
                .map(this::convertToUserBaseInfoDTO)
                .collect(Collectors.toMap(UserBaseInfoDTO::getUid, Function.identity()));
    }

    private UserBaseInfoDTO convertToUserBaseInfoDTO(UserInfo userInfo) {
        UserBaseInfoDTO dto = new UserBaseInfoDTO();
        dto.setUid(userInfo.getUid());
        dto.setNickname(userInfo.getNickname());
        dto.setAvatar(userInfo.getAvatar());
        dto.setLevel(userInfo.getLevel());
        dto.setLevelTitle(userInfo.getLevelTitle());
        // 设置角色，这里需要根据业务逻辑实现
        dto.setRole(determineUserRole(userInfo));
        return dto;
    }

    private String determineUserRole(UserInfo userInfo) {
        // 根据业务逻辑确定用户角色
        if (userInfo.getIsDeveloper() == 1) {
            return "developer";
        }
        return "user";
    }
}
