package com.aoyu.bitsetup.client.service.user.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.aoyu.bitsetup.client.mapper.user.UserAuthMapper;
import com.aoyu.bitsetup.client.mapper.user.UserInfoMapper;
import com.aoyu.bitsetup.client.mapper.user.UserLoginRecordMapper;
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
import com.aoyu.bitsetup.common.utils.IPUtil;
import com.aoyu.bitsetup.common.utils.JwtUtil;
import com.aoyu.bitsetup.common.utils.UserAgentUtil;
import com.aoyu.bitsetup.model.dto.user.UserInfoDTO;
import com.aoyu.bitsetup.model.entity.ip.IPInfo;
import com.aoyu.bitsetup.model.entity.user.UserAuth;
import com.aoyu.bitsetup.model.entity.user.UserInfo;
import com.aoyu.bitsetup.model.entity.user.UserLoginRecord;
import com.aoyu.bitsetup.model.entity.user.UserPoints;
import com.aoyu.bitsetup.model.vo.user.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private UserLoginRecordMapper userLoginRecordMapper;

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
    public UserBaseRespVO register(UserRegisterReqVO userRegisterReqVO, HttpServletRequest httpServletRequest) {

        //判断验证码是否正确
        String emailCaptcha = redisService.getEmailCaptcha(userRegisterReqVO.getAccount());
        log.info("emailCaptcha:{}", emailCaptcha);

        if (emailCaptcha == null || !emailCaptcha.equals(userRegisterReqVO.getEmailVerifyCode())) {
            throw new BusinessException(ResultCode.EMAIL_CAPTCHA_ERROR);
        }
        redisService.deleteEmailCaptcha(userRegisterReqVO.getAccount());

        //校验用户是否存在
        //查询用户
        Boolean res = userAuthMapper.existUserByEmail(userRegisterReqVO.getAccount());
        if (res) {
            log.error("用户已经存在");
            throw new BusinessException(ResultCode.ACCOUNT_EXISTS);
        }

        //设置用户认证信息
        UserAuth userAuth = new UserAuth();
        Long uid = IdWorker.getId();
        userAuth.setEmail(userRegisterReqVO.getAccount());
        userAuth.setUid(uid);
        userAuth.setPassword(BCryptUtil.encode(userRegisterReqVO.getPassword()));
        userAuth.setRole(RoleEnum.USER.getValue());
        //设置用户基本信息
        UserInfo userInfo = new UserInfo();
        String nickname = UserAuthConstant.USER_NICKNAME_PREFIX + RandomUtil.randomString(UserAuthConstant.RANDOM_CHARS, 6);
        userInfo.setUid(uid);
        userInfo.setNickname(nickname);
        //初始化用户积分
        UserPoints userPoints = new UserPoints();
        userPoints.setUid(uid);
        userPointsMapper.insert(userPoints);


        int insertAuthRes = userAuthMapper.insert(userAuth);
        int insertInfoRes = userInfoMapper.insert(userInfo);
        if (insertAuthRes == 0 || insertInfoRes == 0) {
            throw new BusinessException(ResultCode.REGISTER_ERROR);
        }

        //生成登录token
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userRegisterReqVO.getAccount());
        claims.put("role", RoleEnum.USER.getValue());
        String token = jwtUtil.generateToken(String.valueOf(uid), claims);


        //获取登录IP
        String publicIp = IPUtil.getPublicIp();
        IPInfo ipInfo = IPUtil.getIpInfo(publicIp, IPInfo.class);
        log.info("IP详细信息：{}", ipInfo.toString());

        //记录登录信息
        UserLoginRecord userLoginRecord = new UserLoginRecord();
        userLoginRecord.setUid(userAuth.getUid());
        userLoginRecord.setLoginTime(new Date());
        userLoginRecord.setLoginIp(ipInfo.getIp());
        userLoginRecord.setIpProvince(ipInfo.getPro());
        userLoginRecord.setIpCity(ipInfo.getCity());

        String userAgent = httpServletRequest.getHeader("User-Agent");

        String deviceType = UserAgentUtil.getDeviceType(userAgent);
        String browser = UserAgentUtil.getBrowser(userAgent);
        String os = UserAgentUtil.getOs(userAgent);
        userLoginRecord.setUserAgent(userAgent);
        userLoginRecord.setDeviceType(deviceType);
        userLoginRecord.setBrowser(browser);
        userLoginRecord.setOs(os);
        int loginRecordRes = userLoginRecordMapper.insert(userLoginRecord);
        if (loginRecordRes == 0) {
            throw new BusinessException(ResultCode.LOGIN_RECORD_ERROR);
        }

        UserBaseRespVO userRegisterRespVO = new UserBaseRespVO();
        userRegisterRespVO.setUid(uid);
        userRegisterRespVO.setToken(token);
        userRegisterRespVO.setPoints(0);
        userRegisterRespVO.setNickname(nickname);
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
        redisService.setEmailCaptcha(email, code);

        mailService.sendCodeMail(email, UserAuthConstant.MAIL_SUBJECT, UserAuthConstant.MAIL_REGISTER_CONTENT + code + UserAuthConstant.MAIL_EXPIRATION_TIME);

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
    public UserBaseRespVO login(UserLoginReqVO userLoginReqVO, HttpServletRequest httpServletRequest) {

        //查询用户密码
        LambdaQueryWrapper<UserAuth> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(UserAuth::getUid, UserAuth::getPassword)
                .eq(UserAuth::getEmail, userLoginReqVO.getAccount())
                .eq(UserAuth::getStatus, 1)
                .eq(UserAuth::getIsDeleted, 0);

        UserAuth userAuth = userAuthMapper.selectOne(queryWrapper);
        if (userAuth == null) {
            log.error("用户不存在");
            throw new BusinessException(ResultCode.PASSWORD_INVALID);
        } else {
            log.info("用户认证信息：{}", userAuth.toString());
        }

        String password = userLoginReqVO.getPassword();
        String encodePassword = userAuth.getPassword();
        if (!BCryptUtil.matches(password, encodePassword)) {
            log.error("密码校验错误");
            throw new BusinessException(ResultCode.PASSWORD_INVALID);
        }

        UserInfoDTO userInfoDTO = userInfoMapper.selectBaseInfoById(userAuth.getUid());

        UserBaseRespVO userBaseRespVO = new UserBaseRespVO();

        log.info("用户基本信息{}", userInfoDTO.toString());
        BeanUtils.copyProperties(userInfoDTO, userBaseRespVO);
        userBaseRespVO.setPoints(userInfoDTO.getPoints());

        //获取登录IP
        String publicIp = IPUtil.getPublicIp();
        IPInfo ipInfo = IPUtil.getIpInfo(publicIp, IPInfo.class);
        log.info("IP详细信息：{}", ipInfo.toString());

        //记录登录信息
        UserLoginRecord userLoginRecord = new UserLoginRecord();
        userLoginRecord.setUid(userAuth.getUid());
        userLoginRecord.setLoginTime(new Date());
        log.info("现在时间：{}",new Date());
        userLoginRecord.setLoginIp(ipInfo.getIp());
        userLoginRecord.setIpProvince(ipInfo.getPro());
        userLoginRecord.setIpCity(ipInfo.getCity());

        String userAgent = httpServletRequest.getHeader("User-Agent");

        String deviceType = UserAgentUtil.getDeviceType(userAgent);
        String browser = UserAgentUtil.getBrowser(userAgent);
        String os = UserAgentUtil.getOs(userAgent);
        userLoginRecord.setUserAgent(userAgent);
        userLoginRecord.setDeviceType(deviceType);
        userLoginRecord.setBrowser(browser);
        userLoginRecord.setOs(os);
        int res = userLoginRecordMapper.insert(userLoginRecord);
        if (res == 0) {
            throw new BusinessException(ResultCode.LOGIN_RECORD_ERROR);
        }


        //更新本次登录时间和ip
//        LambdaUpdateWrapper<UserAuth> updateWrapper = new LambdaUpdateWrapper<>();
//        updateWrapper
//                .eq(UserAuth::getUid,userAuth.getUid());
//        userAuthMapper.update(updateWrapper);

        //生成登录token
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userAuth.getEmail());
        claims.put("role", RoleEnum.USER.getValue());
        String token = jwtUtil.generateToken(String.valueOf(userInfoDTO.getUid()), claims);
        userBaseRespVO.setToken(token);

        return userBaseRespVO;
    }

    @Override
    public Map<String, UserLoginInfoRespVO> loginInfo(Long uid) {
        LambdaQueryWrapper<UserLoginRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserLoginRecord::getUid, uid)
                .orderByDesc(UserLoginRecord::getLoginTime)
                .last("LIMIT 2");
        List<UserLoginRecord> records = userLoginRecordMapper.selectList(wrapper);
        if (records.isEmpty()) {
            throw new BusinessException(ResultCode.NO_LOGIN_RECORD);
        }

        UserLoginRecord currentRecord = records.get(0);
        UserLoginInfoRespVO currentVO = new UserLoginInfoRespVO();
        currentVO.setBrowser(currentRecord.getBrowser());
        currentVO.setOs(currentRecord.getOs());
        currentVO.setLoginIp(currentRecord.getLoginIp());
        currentVO.setLoginTime(currentRecord.getLoginTime());
        currentVO.setIpProvince(currentRecord.getIpProvince());
        currentVO.setIpCity(currentRecord.getIpCity());

        UserLoginInfoRespVO previousVO;
        if (records.size() > 1) {
            UserLoginRecord previousRecord = records.get(1);
            previousVO = new UserLoginInfoRespVO();
            previousVO.setBrowser(previousRecord.getBrowser());
            previousVO.setOs(previousRecord.getOs());
            previousVO.setLoginIp(previousRecord.getLoginIp());
            previousVO.setLoginTime(previousRecord.getLoginTime());
            previousVO.setIpProvince(previousRecord.getIpProvince());
            previousVO.setIpCity(previousRecord.getIpCity());
        } else {
            previousVO = currentVO;
        }

        Map<String, UserLoginInfoRespVO> result = new HashMap<>();
        result.put("previousLogin", previousVO);
        result.put("currentLogin", currentVO);
        return result;

    }

    @Override
    public void updateEmail(UserUpdateEmailReqVO userUpdateEmailReqVO) {


        //判断验证码是否正确
        String emailCaptcha = redisService.getEmailCaptcha(userUpdateEmailReqVO.getEmail());

        if (emailCaptcha == null || !emailCaptcha.equals(userUpdateEmailReqVO.getEmailVerifyCode())) {
            throw new BusinessException(ResultCode.EMAIL_CAPTCHA_ERROR);
        }
        redisService.deleteEmailCaptcha(userUpdateEmailReqVO.getEmail());

        //校验用户是否存在
        //查询用户
        Boolean res = userAuthMapper.existUserByEmail(userUpdateEmailReqVO.getEmail());
        if (res) {
            log.error("邮箱已被绑定");
            throw new BusinessException(ResultCode.EMAIL_ALREADY_BINDING);
        }


        LambdaUpdateWrapper<UserAuth> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(UserAuth::getEmail, userUpdateEmailReqVO.getEmail())
                .eq(UserAuth::getUid, userUpdateEmailReqVO.getUid())
                .eq(UserAuth::getStatus,1)
                .eq(UserAuth::getIsDeleted, 0);
        int update = userAuthMapper.update(updateWrapper);
        if (update != 1) {
            throw new BusinessException(ResultCode.EMAIL_UPDATE_ERROR);
        }
    }

    @Override
    public void updatePassword(UserUpdatePasswordReqVO updatePasswordReqVO) {

        //判断原密码是否正确
        //查询用户密码
        LambdaQueryWrapper<UserAuth> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(UserAuth::getUid, UserAuth::getPassword)
                .eq(UserAuth::getUid, updatePasswordReqVO.getUid())
                .eq(UserAuth::getStatus, 1)
                .eq(UserAuth::getIsDeleted, 0);

        UserAuth userAuth = userAuthMapper.selectOne(queryWrapper);
        if (userAuth == null) {
            log.error("不存在的用户");
            throw new BusinessException(ResultCode.ACCOUNT_UN_EXISTS);
        }

        String password = updatePasswordReqVO.getOriginalPassword();
        String encodePassword = userAuth.getPassword();
        if (!BCryptUtil.matches(password, encodePassword)) {
            log.error("原密码密码错误");
            throw new BusinessException(ResultCode.ORIGINAL_PASSWORD_INVALID);
        }

        //更新密码
        LambdaUpdateWrapper<UserAuth> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(UserAuth::getPassword, BCryptUtil.encode(updatePasswordReqVO.getNewPassword()))
                .eq(UserAuth::getUid, updatePasswordReqVO.getUid())
                .eq(UserAuth::getStatus,1)
                .eq(UserAuth::getIsDeleted, 0);
        int update = userAuthMapper.update(updateWrapper);
        if (update != 1) {
            throw new BusinessException(ResultCode.UPDATE_PASSWORD_ERROR);
        }

    }

    @Override
    public void deleteUser(UserDeleteReqVO userDeleteReqVO) {
        if(!userDeleteReqVO.getAgreements()){
            throw new BusinessException(ResultCode.NOT_AGREE_AGREEMENTS);
        }

        //判断密码是否正确
        //查询用户密码
        LambdaQueryWrapper<UserAuth> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(UserAuth::getUid, UserAuth::getPassword,UserAuth::getEmail)
                .eq(UserAuth::getUid, userDeleteReqVO.getUid())
                .eq(UserAuth::getStatus, 1)
                .eq(UserAuth::getIsDeleted, 0);

        UserAuth userAuth = userAuthMapper.selectOne(queryWrapper);
        if (userAuth == null) {
            log.error("未知用户");
            throw new BusinessException(ResultCode.ACCOUNT_UN_EXISTS);
        }

        String password = userDeleteReqVO.getPassword();
        String encodePassword = userAuth.getPassword();
        if (!BCryptUtil.matches(password, encodePassword)) {
            log.error("密码错误");
            throw new BusinessException(ResultCode.ORIGINAL_PASSWORD_INVALID);
        }

        //更新账号状态为注销
        LambdaUpdateWrapper<UserAuth> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(UserAuth::getStatus, 2)
                .set(UserAuth::getIsDeleted,1)
                .set(UserAuth::getEmail,userAuth.getEmail() + UserAuthConstant.USER_DELETED_PREFIX + IdUtil.simpleUUID())
                .eq(UserAuth::getUid, userDeleteReqVO.getUid())
                .eq(UserAuth::getStatus,1)
                .eq(UserAuth::getIsDeleted, 0);
        int update = userAuthMapper.update(updateWrapper);
        if (update != 1) {
            throw new BusinessException(ResultCode.ACCOUNT_DELETE_ERROR);
        }

    }

    @Override
    public void resetPassword(UserResetPasswordReqVO userResetPasswordReqVO) {
        //判断验证码是否正确
        String emailCaptcha = redisService.getEmailCaptcha(userResetPasswordReqVO.getAccount());
        log.info("emailCaptcha:{}", emailCaptcha);

        if (emailCaptcha == null || !emailCaptcha.equals(userResetPasswordReqVO.getEmailVerifyCode())) {
            throw new BusinessException(ResultCode.EMAIL_CAPTCHA_ERROR);
        }
        redisService.deleteEmailCaptcha(userResetPasswordReqVO.getAccount());

        //校验用户是否存在
        //查询用户
        //查询用户密码
        LambdaQueryWrapper<UserAuth> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(UserAuth::getUid)
                .eq(UserAuth::getEmail, userResetPasswordReqVO.getAccount())
                .eq(UserAuth::getStatus, 1)
                .eq(UserAuth::getIsDeleted, 0);

        UserAuth userAuth = userAuthMapper.selectOne(queryWrapper);
        if (userAuth == null) {
            log.error("重置密码的用户不存在");
            throw new BusinessException(ResultCode.ACCOUNT_UN_EXISTS);
        }

        //更新密码
        //更新密码
        LambdaUpdateWrapper<UserAuth> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(UserAuth::getPassword, BCryptUtil.encode(userResetPasswordReqVO.getPassword()))
                .eq(UserAuth::getUid, userAuth.getUid())
                .eq(UserAuth::getStatus,1)
                .eq(UserAuth::getIsDeleted, 0);
        int update = userAuthMapper.update(updateWrapper);
        if (update != 1) {
            throw new BusinessException(ResultCode.UPDATE_PASSWORD_ERROR);
        }


    }

}
