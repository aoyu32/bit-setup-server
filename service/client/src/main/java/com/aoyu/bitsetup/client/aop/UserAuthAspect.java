package com.aoyu.bitsetup.client.aop;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.aoyu.bitsetup.client.mapper.user.UserAuthMapper;
import com.aoyu.bitsetup.common.enumeration.ResultCode;
import com.aoyu.bitsetup.common.exception.BusinessException;
import com.aoyu.bitsetup.common.interfaces.UserAuthVerifiable;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName：CaptchaAspect
 * @Author: aoyu
 * @Date: 2025-09-20 14:33
 * @Description: 验证码二次校验切面类
 */

@Aspect
@Component
@Slf4j
public class UserAuthAspect {

    @Autowired
    private CaptchaService captchaService;


    @Pointcut("@annotation(com.aoyu.bitsetup.common.annotation.Auth)")
    public void captchaPoint(){

    }

    @Around("captchaPoint()")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {


        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取方法名
        String methodName = signature.getName();
        // 获取参数数组
        Object[] args = joinPoint.getArgs();

        log.info("请求方法：{}", methodName);
        log.info("请求参数：{}", args);

        UserAuthVerifiable req = (UserAuthVerifiable) args[0];
        log.info("二次验证码：{}",req.getCaptchaCode());
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(req.getCaptchaCode());
        ResponseModel verification = captchaService.verification(captchaVO);
        log.info("二次验证结果：{}",verification);
        if(!verification.getRepCode().equals("0000")){
            log.error("二次行为验证码校验失败");
            throw new BusinessException(ResultCode.CAPTCHA_CODE_ERROR);
        }



        log.info("校验结束");

        // 执行目标方法获取返回值
        return joinPoint.proceed();
    }


}
