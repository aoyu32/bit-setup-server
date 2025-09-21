package com.aoyu.bitsetup.client.controller.user;

import com.aoyu.bitsetup.common.result.Result;
import com.aoyu.bitsetup.model.dto.user.UserInfoDTO;
import com.aoyu.bitsetup.model.vo.user.UserBaseRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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

    @Operation(description = "获取用户基础信息")
    @GetMapping("/base/{uid}")
    public Result<UserBaseRespVO> getUserBaseInfo(@PathVariable String uid){
        return null;
    }

}
