package com.aoyu.bitsetup.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;

/**
 * @ClassName：BCryptUtil
 * @Author: aoyu
 * @Date: 2025-09-20 19:24
 * @Description: 加密工具类
 */

public class BCryptUtil {

    /**
     * BCrypt密码编码器，线程安全
     * strength参数控制加密强度，默认为10
     * 强度越高越安全但计算时间越长，范围4-31
     */
    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(12);

    /**
     * 对明文密码进行BCrypt哈希加密
     * BCrypt会自动生成随机盐并包含在结果中
     *
     * @param rawPassword 明文密码
     * @return 加密后的密码哈希值，包含盐值信息
     * @throws IllegalArgumentException 如果密码为空
     */
    public static String encode(String rawPassword) {
        if (!StringUtils.hasText(rawPassword)) {
            throw new IllegalArgumentException("密码不能为空");
        }
        return PASSWORD_ENCODER.encode(rawPassword);
    }

    /**
     * 验证明文密码与加密密码是否匹配
     *
     * @param rawPassword 明文密码
     * @param encodedPassword 数据库中存储的加密密码
     * @return true表示密码匹配，false表示不匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        if (!StringUtils.hasText(rawPassword) || !StringUtils.hasText(encodedPassword)) {
            return false;
        }
        return PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
    }

    /**
     * 检查密码是否已经被BCrypt加密
     * BCrypt加密后的密码格式：$2a$strength$salt+hash
     *
     * @param password 待检查的密码
     * @return true表示已加密，false表示未加密
     */
    public static boolean isEncoded(String password) {
        if (!StringUtils.hasText(password)) {
            return false;
        }
        // BCrypt密码格式检查：以$2a$、$2b$、$2x$、$2y$开头
        return password.matches("^\\$2[abyxy]\\$\\d{2}\\$.{53}$");
    }

    /**
     * 安全地更新密码（避免重复加密）
     * 如果传入的密码已经是加密格式，直接返回
     * 如果是明文密码，则进行加密
     *
     * @param password 可能是明文也可能是已加密的密码
     * @return 确保加密的密码
     */
    public static String safeEncode(String password) {
        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("密码不能为空");
        }

        if (isEncoded(password)) {
            return password;
        }

        return encode(password);
    }

    /**
     * 生成指定强度的BCrypt编码器
     *
     * @param strength 强度值，范围4-31，推荐10-15
     * @return BCrypt编码器实例
     */
    public static BCryptPasswordEncoder createEncoder(int strength) {
        if (strength < 4 || strength > 31) {
            throw new IllegalArgumentException("BCrypt强度必须在4-31之间");
        }
        return new BCryptPasswordEncoder(strength);
    }

    /**
     * 使用指定强度加密密码
     *
     * @param rawPassword 明文密码
     * @param strength 加密强度
     * @return 加密后的密码
     */
    public static String encodeWithStrength(String rawPassword, int strength) {
        if (!StringUtils.hasText(rawPassword)) {
            throw new IllegalArgumentException("密码不能为空");
        }
        return createEncoder(strength).encode(rawPassword);
    }

    /**
     * 密码强度验证
     *
     * @param password 明文密码
     * @return true表示密码符合强度要求
     */
    public static boolean validatePasswordStrength(String password) {
        if (!StringUtils.hasText(password)) {
            return false;
        }

        // 至少8位，包含大小写字母、数字
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,}$";
        return password.matches(pattern);
    }

    /**
     * 获取密码强度等级
     *
     * @param password 明文密码
     * @return 强度等级：0-弱，1-中，2-强
     */
    public static int getPasswordStrengthLevel(String password) {
        if (!StringUtils.hasText(password)) {
            return 0;
        }

        int score = 0;

        // 长度检查
        if (password.length() >= 8) score++;
        if (password.length() >= 12) score++;

        // 复杂度检查
        if (password.matches(".*[a-z].*")) score++; // 小写字母
        if (password.matches(".*[A-Z].*")) score++; // 大写字母
        if (password.matches(".*\\d.*")) score++;   // 数字
        if (password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) score++; // 特殊字符

        // 返回强度等级
        if (score >= 5) return 2; // 强
        if (score >= 3) return 1; // 中
        return 0; // 弱
    }

}
