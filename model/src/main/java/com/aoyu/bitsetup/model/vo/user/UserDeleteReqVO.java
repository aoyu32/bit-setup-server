package com.aoyu.bitsetup.model.vo.user;

import lombok.Data;

/**
 * @ClassName：UserDeleteReqVO
 * @Author: aoyu
 * @Date: 2025-10-05 00:17
 * @Description: 用户注销请求VO
 */

@Data
public class UserDeleteReqVO {

    private Long uid;
    private String password;
    private Boolean agreements;


}
