package com.aoyu.bitsetup.client.service.user;

import com.aoyu.bitsetup.model.vo.user.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.Map;

/**
 * @InterfaceName：UserAuth
 * @Author: aoyu
 * @Date: 2025/9/20 下午12:42
 * @Description:
 */

public interface UserAuthService {
    /**
     * @description: 用户注册
     * @author: aoyu
     * @date: 2025/9/20 下午1:46
     * @param:
     * @return:
     */
    UserBaseRespVO register(UserRegisterReqVO userBaseReqVO,HttpServletRequest httpServletRequest);

    /**
     * @description: 发送验证码
     * @author: aoyu
     * @date: 2025/9/20 下午4:31
     * @param:
     * @return:
     */
    void sendCode(String email);


    /**
     * @description: 用户登录
     * @author: aoyu
     * @date: 2025/9/21 下午4:06
     * @param:
     * @return:
     */
    UserBaseRespVO login(@Valid UserLoginReqVO userLoginReqVO, HttpServletRequest httpServletRequest);

    Map<String,UserLoginInfoRespVO> loginInfo(Long uid);

    void updateEmail(UserUpdateEmailReqVO updateEmailReqVO);

    void updatePassword(UserUpdatePasswordReqVO updatePasswordReqVO);

    void deleteUser(UserDeleteReqVO userDeleteReqVO);

    void resetPassword(UserResetPasswordReqVO userResetPasswordReqVO);
}
