package com.aoyu.bitsetup.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName：RangeStringUtil
 * @Author: aoyu
 * @Date: 2025-09-16 23:21
 * @Description: 范围字符串处理工具类
 */
public class RangeStringUtil {

    /**
     * 解析范围字符串，返回最小值和最大值
     * 支持格式：>123, <456, 123-456, >=123, <=456
     *
     * @param data 范围字符串
     * @return Map包含min和max键，值为字符串类型
     */
    public static Map<String, Object> getMinAndMax(String data) {
        Map<String, Object> map = new HashMap<>();

        if (data == null || data.trim().isEmpty()) {
            return map;
        }

        data = data.trim();

        // 处理 > 开头的情况（最大值为null）
        if (data.startsWith(">=")) {
            String value = data.substring(2).trim();
            map.put("min", value);
            map.put("max", null);
        } else if (data.startsWith(">")) {
            String value = data.substring(1).trim();
            map.put("min", value);
            map.put("max", null);
        }
        // 处理 < 开头的情况（最小值为null）
        else if (data.startsWith("<=")) {
            String value = data.substring(2).trim();
            map.put("min", null);
            map.put("max", value);
        } else if (data.startsWith("<")) {
            String value = data.substring(1).trim();
            map.put("min", null);
            map.put("max", value);
        }
        // 处理范围格式 123-456
        else if (data.contains("-")) {
            String[] parts = data.split("-", 2);
            if (parts.length == 2) {
                map.put("min", parts[0].trim());
                map.put("max", parts[1].trim());
            }
        }
        // 单个数值，既是最小值也是最大值
        else {
            map.put("min", data);
            map.put("max", data);
        }

        return map;
    }

    /**
     * 解析范围字符串并转换为指定的数值类型
     *
     * @param data 范围字符串
     * @param type 目标类型 (Integer.class, Double.class, Long.class等)
     * @return Map包含min和max键，值为指定类型
     */
    @SuppressWarnings("unchecked")
    public static <T extends Number> Map<String, T> getMinAndMax(String data, Class<T> type) {
        Map<String, Object> stringMap = getMinAndMax(data);
        Map<String, T> result = new HashMap<>();

        // 转换最小值
        Object minObj = stringMap.get("min");
        if (minObj != null) {
            result.put("min", convertToType(minObj.toString(), type));
        } else {
            result.put("min", null);
        }

        // 转换最大值
        Object maxObj = stringMap.get("max");
        if (maxObj != null) {
            result.put("max", convertToType(maxObj.toString(), type));
        } else {
            result.put("max", null);
        }

        return result;
    }

    /**
     * 将字符串转换为指定的数值类型
     */
    @SuppressWarnings("unchecked")
    private static <T extends Number> T convertToType(String value, Class<T> type) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            if (type == Integer.class) {
                return (T) Integer.valueOf(value.trim());
            } else if (type == Double.class) {
                return (T) Double.valueOf(value.trim());
            } else if (type == Long.class) {
                return (T) Long.valueOf(value.trim());
            } else if (type == Float.class) {
                return (T) Float.valueOf(value.trim());
            } else if (type == Short.class) {
                return (T) Short.valueOf(value.trim());
            } else if (type == Byte.class) {
                return (T) Byte.valueOf(value.trim());
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("无法将 '" + value + "' 转换为 " + type.getSimpleName() + " 类型", e);
        }

        throw new IllegalArgumentException("不支持的数值类型: " + type.getSimpleName());
    }
}