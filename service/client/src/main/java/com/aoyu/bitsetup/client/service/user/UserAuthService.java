package com.aoyu.bitsetup.client.service.user;

import com.aoyu.bitsetup.model.vo.user.UserRegisterReqVO;
import com.aoyu.bitsetup.model.vo.user.UserRegisterRespVO;

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
    UserRegisterRespVO register(UserRegisterReqVO userRegisterReqVO);

    /**
     * @description: 发送验证码
     * @author: aoyu
     * @date: 2025/9/20 下午4:31
     * @param:
     * @return:
     */
    void sendCode(String email);



}
