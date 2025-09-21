package com.aoyu.bitsetup.client.service.user;

import com.aoyu.bitsetup.model.vo.user.UserBaseRespVO;
import com.aoyu.bitsetup.model.vo.user.UserLoginReqVO;
import com.aoyu.bitsetup.model.vo.user.UserRegisterReqVO;
import jakarta.validation.Valid;

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
    UserBaseRespVO register(UserRegisterReqVO userBaseReqVO);

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
    UserBaseRespVO login(@Valid UserLoginReqVO userLoginReqVO);
}
