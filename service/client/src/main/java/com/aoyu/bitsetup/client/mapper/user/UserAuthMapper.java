package com.aoyu.bitsetup.client.mapper.user;

import com.aoyu.bitsetup.model.entity.user.UserAuth;
import com.aoyu.bitsetup.model.entity.user.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @InterfaceName：UserAuthMapper
 * @Author: aoyu
 * @Date: 2025/9/20 下午12:43
 * @Description:
 */

@Mapper
public interface UserAuthMapper extends BaseMapper<UserAuth> {

    /**
     * @description: 根据邮箱判断查询用户是否存在
     * @author: aoyu
     * @date: 2025/9/20 下午3:07
     * @param:
     * @return:
     */
    @Select("select count(*)>0 from user_auth where email = #{email} and is_deleted = 0")
    Boolean existUserByEmail(String email);

}
