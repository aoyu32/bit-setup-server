package com.aoyu.bitsetup.model.vo.user;

import lombok.Data;

/**
 * @ClassName：UserModifyReqVO
 * @Author: aoyu
 * @Date: 2025-10-03 13:13
 * @Description: 用户信息修改VO
 */

@Data
public class UserUpdateReqVO {

    private String uid;
    private String avatar;
    private String nickname;
    private String bio;
    private String gender;
    private String province;
    private String city;
    private String career;

}
