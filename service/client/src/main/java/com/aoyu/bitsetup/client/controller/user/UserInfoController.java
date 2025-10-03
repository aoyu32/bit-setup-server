package com.aoyu.bitsetup.client.controller.user;

import com.aoyu.bitsetup.client.service.user.UserInfoService;
import com.aoyu.bitsetup.common.result.Result;
import com.aoyu.bitsetup.common.utils.ThreadLocalUtil;
import com.aoyu.bitsetup.model.dto.user.UserInfoDTO;
import com.aoyu.bitsetup.model.entity.user.UserInfo;
import com.aoyu.bitsetup.model.vo.user.UserBaseRespVO;
import com.aoyu.bitsetup.model.vo.user.UserUpdateReqVO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName：UserInfoController
 * @Author: aoyu
 * @Date: 2025-09-21 15:27
 * @Description: 应用信息接口
 */

@Tag(name = "用户信息")
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @Operation(description = "获取用户基础信息")
    @GetMapping("/base")
    public Result<UserInfoDTO> getBaseInfo(){
        log.info("获取用户uid为：{}基本信息", ThreadLocalUtil.get("uid"));
        UserInfoDTO userInfoDTO = userInfoService.getBaseInfo((Long)ThreadLocalUtil.get("uid"));
        return Result.success(userInfoDTO);
    }


    @Operation(description = "修改用户信息")
    @PostMapping("/update")
    public Result<?> updateUserInfo(@RequestBody UserUpdateReqVO userUpdateReqVO){
        log.info("用户uid为：{}请求更新用户信息：{}",userUpdateReqVO.getUid(),userUpdateReqVO);
        userInfoService.updateBaseInfo(userUpdateReqVO);
        return Result.success();
    }

    @Operation(description = "用户上传头像")
    @PostMapping("/upload/avatar")
    public Result<String> uploadAvatar(MultipartFile file,String uid){
        log.info("用户uid为：{}请求上传头像",uid);
        String avatarUrl = userInfoService.uploadAvatar(file, uid);
        return Result.success(avatarUrl);
    }

}
