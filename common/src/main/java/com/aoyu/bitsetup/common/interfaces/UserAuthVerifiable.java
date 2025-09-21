package com.aoyu.bitsetup.common.interfaces;

/**
 * @InterfaceName：CaptchaVerifiable
 * @Author: aoyu
 * @Date: 2025/9/20 下午2:56
 * @Description:
 */

public interface UserAuthVerifiable {
    String getCaptchaCode();
    String getAccount();
}
