package com.aoyu.bitsetup.client.service.user.impl;

import cn.hutool.core.util.RandomUtil;
import com.aoyu.bitsetup.client.mapper.user.UserAuthMapper;
import com.aoyu.bitsetup.client.mapper.user.UserInfoMapper;
import com.aoyu.bitsetup.client.mapper.user.UserPointsMapper;
import com.aoyu.bitsetup.client.service.mail.MailService;
import com.aoyu.bitsetup.client.service.redis.RedisService;
import com.aoyu.bitsetup.client.service.user.UserAuthService;
import com.aoyu.bitsetup.common.annotation.Auth;
import com.aoyu.bitsetup.common.constants.UserAuthConstant;
import com.aoyu.bitsetup.common.enumeration.ResultCode;
import com.aoyu.bitsetup.common.enumeration.RoleEnum;
import com.aoyu.bitsetup.common.exception.BusinessException;
import com.aoyu.bitsetup.common.utils.BCryptUtil;
import com.aoyu.bitsetup.common.utils.JwtUtil;
import com.aoyu.bitsetup.model.entity.user.UserAuth;
import com.aoyu.bitsetup.model.entity.user.UserInfo;
import com.aoyu.bitsetup.model.entity.user.UserPoints;
import com.aoyu.bitsetup.model.vo.user.UserRegisterReqVO;
import com.aoyu.bitsetup.model.vo.user.UserRegisterRespVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName：UserAuthServiceImpl
 * @Author: aoyu
 * @Date: 2025-09-20 12:42
 * @Description: 用户认证接口实现类
 */

@Service
@Slf4j
public class UserAuthServiceImpl implements UserAuthService {


    @Autowired
    private UserAuthMapper userAuthMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserPointsMapper userPointsMapper;

    @Autowired
    private MailService mailService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private JwtUtil jwtUtil;




    /**
     * @description: 用户注册
     * @author: aoyu
     * @date: 2025/9/20 下午2:00
     * @param:
     * @return:
     */
    @Override
    @Auth
    @Transactional
    public UserRegisterRespVO register(UserRegisterReqVO userRegisterReqVO) {

        //判断验证码是否正确
        String emailCaptcha = redisService.getEmailCaptcha(userRegisterReqVO.getEmail());

        if(emailCaptcha == null ||!emailCaptcha.equals(userRegisterReqVO.getEmailVerifyCode())){
            throw new BusinessException(ResultCode.EMAIL_CAPTCHA_ERROR);
        }
        redisService.deleteEmailCaptcha(userRegisterReqVO.getEmail());

        //设置用户认证信息
        UserAuth userAuth = new UserAuth();
        Long uid = IdWorker.getId();
        userAuth.setEmail(userRegisterReqVO.getEmail());
        userAuth.setUid(uid);
        userAuth.setPassword(BCryptUtil.encode(userRegisterReqVO.getPassword()));
        userAuth.setRole(RoleEnum.USER.getValue());
        //设置用户基本信息
        UserInfo userInfo = new UserInfo();
        String nickname = UserAuthConstant.USER_NICKNAME_PREFIX + RandomUtil.randomString(UserAuthConstant.RANDOM_CHARS,6);
        userInfo.setUid(uid);
        userInfo.setNickname(nickname);
        //初始化用户积分
        UserPoints userPoints = new UserPoints();
        userPoints.setUid(uid);
        userPointsMapper.insert(userPoints);


        int insertAuthRes = userAuthMapper.insert(userAuth);
        int insertInfoRes = userInfoMapper.insert(userInfo);
        if(insertAuthRes == 0 || insertInfoRes == 0){
            throw new BusinessException(ResultCode.REGISTER_ERROR);
        }

       //生成登录token
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userRegisterReqVO.getEmail());
        claims.put("role", RoleEnum.USER.getValue());
        String token = jwtUtil.generateToken(String.valueOf(uid),claims);

        UserRegisterRespVO userRegisterRespVO = new UserRegisterRespVO();
        userRegisterRespVO.setUid(uid);
        userRegisterRespVO.setToken(token);
        userRegisterRespVO.setPoints(0);
        userRegisterRespVO.setNickname(nickname);
        userRegisterRespVO.setRole(RoleEnum.USER.getValue());
        userRegisterRespVO.setAvatar(UserAuthConstant.DEFAULT_AVATAR);

        return userRegisterRespVO;
    }

    @Override
    public void sendCode(String email) {

        String code = RandomUtil.randomNumbers(6);

        //存入redis
        redisService.setEmailCaptcha(email,code);

        mailService.sendCodeMail(email, UserAuthConstant.MAIL_SUBJECT,UserAuthConstant.MAIL_REGISTER_CONTENT+code+ UserAuthConstant.MAIL_EXPIRATION_TIME);

    }

}
