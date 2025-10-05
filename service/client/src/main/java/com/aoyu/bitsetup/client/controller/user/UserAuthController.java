package com.aoyu.bitsetup.client.controller.user;

import com.aoyu.bitsetup.client.service.user.UserAuthService;
import com.aoyu.bitsetup.common.result.Result;
import com.aoyu.bitsetup.model.vo.user.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public Result<UserBaseRespVO> register(@Valid @RequestBody UserRegisterReqVO userRegisterReqVO,HttpServletRequest httpServletRequest) {
        log.info("用户请求注册的数据：{}", userRegisterReqVO.toString());
        UserBaseRespVO register = userAuthService.register(userRegisterReqVO, httpServletRequest);
        return Result.success(register);
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
    public Result<UserBaseRespVO> login(@RequestBody @Valid UserLoginReqVO userLoginReqVO, HttpServletRequest httpServletRequest){
        log.info("用户登录，账号{}",userLoginReqVO.toString());
        UserBaseRespVO loginRespVO = userAuthService.login(userLoginReqVO,httpServletRequest);
        return Result.success(loginRespVO);
    }

    @Operation(description = "登录信息")
    @GetMapping("/login/info")
    public Result<?> loginInfo(@RequestParam String uid){
        log.info("用户uid为：{}请求获取登录记录",uid);
        Map<String, UserLoginInfoRespVO> loginMap = userAuthService.loginInfo(Long.valueOf(uid));
        return Result.success(loginMap);
    }

    @Operation(description = "更新绑定邮箱")
    @PostMapping("/email/update")
    public Result<?> updateEmail(@Valid @RequestBody UserUpdateEmailReqVO updateEmailReqVO){
        log.info("用户uid为：{}请求更新邮箱",updateEmailReqVO.getUid());
        userAuthService.updateEmail(updateEmailReqVO);
        return Result.success();
    }

    @Operation(description = "更新密码")
    @PostMapping("/pwd/update")
    public Result<?> updatePassword(@Valid @RequestBody UserUpdatePasswordReqVO updatePasswordReqVO){
        log.info("用户uid为：{}请求更新密码",updatePasswordReqVO.getUid());
        userAuthService.updatePassword(updatePasswordReqVO);
        return Result.success();
    }

    @Operation(description = "注销账号")
    @PostMapping("/account/delete")
    public Result<?> deleteAccount(@RequestBody UserDeleteReqVO userDeleteReqVO){
        log.info("用户uid为：{}请求注销账号",userDeleteReqVO.getUid());
        userAuthService.deleteUser(userDeleteReqVO);
        return Result.success();
    }

    @Operation(description = "用户重置密码")
    @PostMapping("/reset")
    public Result<?> reset(@Valid @RequestBody UserResetPasswordReqVO userResetPasswordReqVO) {
        log.info("用户请求重置密码的数据：{}", userResetPasswordReqVO.toString());
        userAuthService.resetPassword(userResetPasswordReqVO);
        return Result.success();
    }


}
