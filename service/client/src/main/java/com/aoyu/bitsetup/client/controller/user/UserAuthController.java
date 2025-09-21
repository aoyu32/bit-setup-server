package com.aoyu.bitsetup.client.controller.user;

import com.aoyu.bitsetup.client.service.user.UserAuthService;
import com.aoyu.bitsetup.common.annotation.Auth;
import com.aoyu.bitsetup.common.result.Result;
import com.aoyu.bitsetup.model.vo.user.UserBaseRespVO;
import com.aoyu.bitsetup.model.vo.user.UserLoginReqVO;
import com.aoyu.bitsetup.model.vo.user.UserRegisterReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName：UserAuthController
 * @Author: aoyu
 * @Date: 2025-09-20 12:41
 * @Description: 用户认证接口
 */

@Tag(name = "用户认证", description = "用户认证相关接口")
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class UserAuthController {

    @Resource
    private UserAuthService userAuthService;

    @Operation(description = "用户注册")
    @PostMapping("/register")
    public Result<UserBaseRespVO> register(@Valid @RequestBody UserRegisterReqVO userRegisterReqVO) {
        log.info("用户请求注册的数据：{}", userRegisterReqVO.toString());
        UserBaseRespVO registerResult = userAuthService.register(userRegisterReqVO);
        log.info("返回的注册结果：{}", registerResult.toString());
        return Result.success(registerResult);
    }

    @Operation(description = "获取验证码")
    @GetMapping("/code")
    @Validated
    public Result<?> sendCode(@Email(message = "邮箱格式不正确") @RequestParam String email) {
        log.info("发送验证码给{}", email);
        userAuthService.sendCode(email);
        return Result.success();
    }

    @Operation(description = "用户登录")
    @PostMapping("/login")
    public Result<UserBaseRespVO> login(@RequestBody @Valid UserLoginReqVO userLoginReqVO){
        log.info("用户登录，账号{}",userLoginReqVO.toString());
        UserBaseRespVO loginRespVO = userAuthService.login(userLoginReqVO);
        return Result.success(loginRespVO);
    }


}
