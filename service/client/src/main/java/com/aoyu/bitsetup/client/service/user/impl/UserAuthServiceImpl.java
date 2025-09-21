package com.aoyu.bitsetup.client.service.user.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.anji.captcha.util.DateUtils;
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
import com.aoyu.bitsetup.model.dto.user.UserInfoDTO;
import com.aoyu.bitsetup.model.entity.user.UserAuth;
import com.aoyu.bitsetup.model.entity.user.UserInfo;
import com.aoyu.bitsetup.model.entity.user.UserPoints;
import com.aoyu.bitsetup.model.vo.user.UserBaseRespVO;
import com.aoyu.bitsetup.model.vo.user.UserLoginReqVO;
import com.aoyu.bitsetup.model.vo.user.UserRegisterReqVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
    public UserBaseRespVO register(UserRegisterReqVO userRegisterReqVO) {



        //判断验证码是否正确
        String emailCaptcha = redisService.getEmailCaptcha(userRegisterReqVO.getAccount());
        log.info("emailCaptcha:{}", emailCaptcha);

        if(emailCaptcha == null ||!emailCaptcha.equals(userRegisterReqVO.getEmailVerifyCode())){
            throw new BusinessException(ResultCode.EMAIL_CAPTCHA_ERROR);
        }
        redisService.deleteEmailCaptcha(userRegisterReqVO.getAccount());

        //校验用户是否存在
        //查询用户
        Boolean res = userAuthMapper.existUserByEmail(userRegisterReqVO.getAccount());
        if(res){
            log.error("用户已经存在");
            throw new BusinessException(ResultCode.ACCOUNT_EXISTS);
        }

        //设置用户认证信息
        UserAuth userAuth = new UserAuth();
        Long uid = IdWorker.getId();
        userAuth.setEmail(userRegisterReqVO.getAccount());
        userAuth.setUid(uid);
        userAuth.setLastLoginTime(DateUtil.date());
        userAuth.setAccessIp("114.114.114.114");
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
        claims.put("email", userRegisterReqVO.getAccount());
        claims.put("role", RoleEnum.USER.getValue());
        String token = jwtUtil.generateToken(String.valueOf(uid),claims);

        UserBaseRespVO userRegisterRespVO = new UserBaseRespVO();
        userRegisterRespVO.setUid(uid);
        userRegisterRespVO.setToken(token);
        userRegisterRespVO.setPoints(0);
        userRegisterRespVO.setNickname(nickname);
        userRegisterRespVO.setLastLoginTime(userAuth.getLastLoginTime());
        userRegisterRespVO.setAccessIp(userAuth.getAccessIp());
        userRegisterRespVO.setRole(RoleEnum.USER.getValue());
        userRegisterRespVO.setAvatar(UserAuthConstant.DEFAULT_AVATAR);

        return userRegisterRespVO;
    }

    /**
     * @description: 获取邮箱验证码
     * @author: aoyu
     * @date: 2025/9/21 下午4:55
     * @param:
     * @return:
     */
    @Override
    public void sendCode(String email) {

        String code = RandomUtil.randomNumbers(6);

        //存入redis
        redisService.setEmailCaptcha(email,code);

        mailService.sendCodeMail(email, UserAuthConstant.MAIL_SUBJECT,UserAuthConstant.MAIL_REGISTER_CONTENT+code+ UserAuthConstant.MAIL_EXPIRATION_TIME);

    }

    /**
     * @description: 用户登录
     * @author: aoyu
     * @date: 2025/9/21 下午4:55
     * @param:
     * @return:
     */
    @Override
    @Auth
    public UserBaseRespVO login(UserLoginReqVO userLoginReqVO) {

        //查询用户密码
        LambdaQueryWrapper<UserAuth> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(UserAuth::getUid,UserAuth::getPassword)
                .eq(UserAuth::getEmail,userLoginReqVO.getAccount())
                .eq(UserAuth::getStatus,1)
                .eq(UserAuth::getIsDeleted,0);

        UserAuth userAuth = userAuthMapper.selectOne(queryWrapper);
        if(userAuth == null){
            log.error("用户不存在");
            throw new BusinessException(ResultCode.ACCOUNT_UN_EXISTS);
        }else {
            log.info("用户认证信息：{}",userAuth.toString());
        }

        String password = userLoginReqVO.getPassword();
        String encodePassword = userAuth.getPassword();
        if(!BCryptUtil.matches(password,encodePassword)){
            log.error("密码校验错误");
            throw new BusinessException(ResultCode.PASSWORD_INVALID);
        }

        UserInfoDTO userInfoDTO = userInfoMapper.selectBaseInfoById(userAuth.getUid());

        UserBaseRespVO userBaseRespVO = new UserBaseRespVO();

        log.info("用户基本信息{}",userInfoDTO.toString());
        BeanUtils.copyProperties(userInfoDTO, userBaseRespVO);
        userBaseRespVO.setPoints(userInfoDTO.getPoints());

        //更新本次登录时间和ip
        LambdaUpdateWrapper<UserAuth> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .set(UserAuth::getLastLoginTime, DateUtil.date()) // 指定要更新的字段和值
                .set(UserAuth::getAccessIp, "114.143.123.123")
                .eq(UserAuth::getUid,userAuth.getUid());
        userAuthMapper.update(updateWrapper);

        //生成登录token
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userAuth.getEmail());
        claims.put("role", RoleEnum.USER.getValue());
        String token = jwtUtil.generateToken(String.valueOf(userInfoDTO.getUid()),claims);
        userBaseRespVO.setToken(token);

        return userBaseRespVO;
    }

}
