package com.aoyu.bitsetup.client.controller.user;

import com.aoyu.bitsetup.client.service.user.UserInfoService;
import com.aoyu.bitsetup.common.result.Result;
import com.aoyu.bitsetup.common.utils.ThreadLocalUtil;
import com.aoyu.bitsetup.model.dto.user.UserInfoDTO;
import com.aoyu.bitsetup.model.vo.user.UserBaseRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Result<UserBaseRespVO> getBaseInfo(){
        log.info("获取用户uid为：{}基本信息", ThreadLocalUtil.get("uid"));
        UserBaseRespVO baseInfo = userInfoService.getBaseInfo((Long)ThreadLocalUtil.get("uid"));
        return Result.success(baseInfo);
    }

}
