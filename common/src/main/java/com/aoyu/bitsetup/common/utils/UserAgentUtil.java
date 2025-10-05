package com.aoyu.bitsetup.common.utils;



/**
 * @ClassName：UserAgentUtil
 * @Author: aoyu
 * @Date: 2025-10-04 17:37
 * @Description: 用户代理解析类
 */

import ua_parser.Client;
import ua_parser.Parser;

/**
 * 用户代理解析工具类
 */
public class UserAgentUtil {

    private static final Parser UA_PARSER = new Parser();

    /**
     * 获取设备类型
     *
     * @param userAgent User-Agent字符串
     * @return 设备类型: Desktop/Mobile/Tablet/Bot
     */
    public static String getDeviceType(String userAgent) {
        if (userAgent == null || userAgent.trim().isEmpty()) {
            return "Unknown";
        }

        Client client = UA_PARSER.parse(userAgent);
        String deviceFamily = client.device.family;

        if ("Spider".equalsIgnoreCase(deviceFamily)) {
            return "Bot";
        }

        // 判断是否为移动设备
        if (deviceFamily != null && !deviceFamily.equals("Other")) {
            String lowerFamily = deviceFamily.toLowerCase();
            if (lowerFamily.contains("mobile") || lowerFamily.contains("phone")) {
                return "Mobile";
            }
            if (lowerFamily.contains("tablet") || lowerFamily.contains("pad")) {
                return "Tablet";
            }
            return "Mobile";
        }

        // 通过OS判断设备类型
        String osFamily = client.os.family;
        if (osFamily != null) {
            String lowerOs = osFamily.toLowerCase();
            if (lowerOs.contains("android") || lowerOs.contains("ios") ||
                    lowerOs.contains("windows phone") || lowerOs.contains("blackberry")) {
                return "Mobile";
            }
        }

        return "Desktop";
    }

    /**
     * 获取浏览器信息
     *
     * @param userAgent User-Agent字符串
     * @return 浏览器名称及版本,如: Chrome 120.0.0
     */
    public static String getBrowser(String userAgent) {
        if (userAgent == null || userAgent.trim().isEmpty()) {
            return "Unknown";
        }

        Client client = UA_PARSER.parse(userAgent);
        StringBuilder browser = new StringBuilder();

        if (client.userAgent.family != null) {
            browser.append(client.userAgent.family);

            // 拼接版本号
            if (client.userAgent.major != null) {
                browser.append(" ").append(client.userAgent.major);
                if (client.userAgent.minor != null) {
                    browser.append(".").append(client.userAgent.minor);
                    if (client.userAgent.patch != null) {
                        browser.append(".").append(client.userAgent.patch);
                    }
                }
            }
        }

        return browser.length() > 0 ? browser.toString() : "Unknown";
    }

    /**
     * 获取操作系统信息
     *
     * @param userAgent User-Agent字符串
     * @return 操作系统名称及版本,如: Windows 10
     */
    public static String getOs(String userAgent) {
        if (userAgent == null || userAgent.trim().isEmpty()) {
            return "Unknown";
        }

        Client client = UA_PARSER.parse(userAgent);
        StringBuilder os = new StringBuilder();

        if (client.os.family != null) {
            os.append(client.os.family);

            // 拼接版本号
            if (client.os.major != null) {
                os.append(" ").append(client.os.major);
                if (client.os.minor != null) {
                    os.append(".").append(client.os.minor);
                    if (client.os.patch != null) {
                        os.append(".").append(client.os.patch);
                    }
                }
            }
        }

        return os.length() > 0 ? os.toString() : "Unknown";
    }

}