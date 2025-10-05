package com.aoyu.bitsetup.common.utils;

/**
 * @ClassName：IPUtil
 * @Author: aoyu
 * @Date: 2025-10-03 23:06
 * @Description: 获取ip地址工具类
 */

import com.aoyu.bitsetup.common.exception.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ServiceException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class IPUtil {


    //请求IP地址服务url
    private static String ipInfoRequestUrl; // 静态字段

    @Value("${ip.ip_platform}")
    private String ipUrl; // 非静态字段用于注入

    @PostConstruct // 在 Bean 初始化后执行
    public void init() {
        ipInfoRequestUrl = this.ipUrl; // 赋值给静态变量
    }


    private static final RestTemplate restTemplate = new RestTemplate();


    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("X-Forwarded-For");
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    if (inet != null) {
                        ipAddress = inet.getHostAddress();
                    }
                }
            }
            // 对于多个代理的情况，第一个IP为客户端真实IP，多个IP以','分隔
            if (ipAddress != null && ipAddress.length() > 15) {
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }

    /**
     * 获取ip详细详细
     *
     * @return
     */
    /**
     * 获取IP详细信息
     * @param ipAddr 要查询的IP地址(非空)
     * @param responseType 返回类型
     * @return 包含IP信息的指定类型对象
     * @throws ServiceException 当查询失败时抛出
     */
    public static  <T> T getIpInfo(String ipAddr, Class<T> responseType) {
        try {
            // 参数校验
            if (ipAddr == null || ipAddr.trim().isEmpty()) {
                throw new IllegalArgumentException("IP地址不能为空");
            }

            // 构建URL
            String encodedIp = URLEncoder.encode(ipAddr, StandardCharsets.UTF_8);

            log.info("request ip base url:{}",ipInfoRequestUrl);
            log.info("request ip:{}",encodedIp);
            String requestUrl = String.format("%s?ip=%s&json=true", ipInfoRequestUrl, encodedIp);

            log.debug("请求IP信息: {}", requestUrl);
            String forObject = restTemplate.getForObject(requestUrl, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(forObject, responseType);


        } catch (HttpClientErrorException e) {
            throw new BusinessException(700,"客户端请求错误: " + e.getStatusCode());
        } catch (HttpServerErrorException e) {
            throw new BusinessException(701,"服务端错误: " + e.getStatusCode());
        } catch (ResourceAccessException e) {
            throw new BusinessException(702,"网络连接失败: " + e.getMessage());
        } catch (Exception e) {
            throw new BusinessException(703,"获取IP信息失败: " + e.getMessage());
        }
    }


    public static String getPublicIp() {
        String url = "http://ip.3322.net/"; // 获取 IP 的接口
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody().trim(); // 返回公网 IP 地址
            } else {
                return "Failed to retrieve public IP: " + response.getStatusCode();
            }
        } catch (Exception e) {
            return "Error retrieving public IP: " + e.getMessage();
        }
    }
}