package com.aoyu.bitsetup.model.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName：UserLoginInfoRespVO
 * @Author: aoyu
 * @Date: 2025-10-03 23:01
 * @Description: 用户登录信息响应VO
 */

@Data
public class UserLoginInfoRespVO {

    private String browser;
    private String os;
    private String loginIp;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date loginTime;
    private String ipProvince;
    private String ipCity;

}
