package com.aoyu.bitsetup.client.controller.user;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.aoyu.bitsetup.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/captcha")
@Tag(name = "人机验证",description = "人机检测验证码相关接口，包括获取滑块验证码的背景图片，滑块图片，以及对滑动数据的检测，以及二次校验")
@Slf4j
public class CaptchaController {
    @Autowired
    private CaptchaService captchaService;

    @PostMapping("/get")
    @Operation(summary = "获取滑块验证码",description = "获取滑块验证码的背景图和滑块图以及校验数据")
    public Result<ResponseModel> get(@RequestBody CaptchaVO data, HttpServletRequest request) {
        log.info("获取验证码：{}",data.getCaptchaType());
        assert request.getRemoteHost()!=null;
        data.setBrowserInfo(getRemoteId(request));
        return Result.success(captchaService.get(data));
    }

    @PostMapping("/check")
    @Operation(summary = "校验滑块验证码",description = "校验滑块验证码的有效性")
    public Result<ResponseModel> check(@RequestBody CaptchaVO data, HttpServletRequest request) {
        data.setBrowserInfo(getRemoteId(request));
        return Result.success(captchaService.check(data));
    }

    /***
     * 服务端验证接口，独立部署的场景使用，集成部署的场景：服务内部调用，不需要调用此接口可注释掉
     * @param data
     * @param request
     * @return
     */
    @PostMapping("/verify")
    @Operation(summary = "对滑块验证码的二次校验",description = "对滑块验证进行二次校验")
    public Result<ResponseModel> verify(@RequestBody CaptchaVO data, HttpServletRequest request) {
        return Result.success( captchaService.verification(data));
    }

    public static final String getRemoteId(HttpServletRequest request) {
        String xfwd = request.getHeader("X-Forwarded-For");
        String ip = getRemoteIpFromXfwd(xfwd);
        String ua = request.getHeader("user-agent");
        if (StringUtils.isNotBlank(ip)) {
            return ip + ua;
        }
        return request.getRemoteAddr() + ua;
    }

    private static String getRemoteIpFromXfwd(String xfwd) {
        if (StringUtils.isNotBlank(xfwd)) {
            String[] ipList = xfwd.split(",");
            return StringUtils.trim(ipList[0]);
        }
        return null;
    }
}
