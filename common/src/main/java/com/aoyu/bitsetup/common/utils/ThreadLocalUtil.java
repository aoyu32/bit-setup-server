package com.aoyu.bitsetup.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName：ThreadLocalUtils
 * @Author: aoyu
 * @Date: 2025-09-22 00:05
 * @Description: 线程上下文工具类
 */

public class ThreadLocalUtil {
    // 使用 ThreadLocal 存储键值对
    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal<>();

    // 初始化 ThreadLocal 中的 Map
    private static void ensureInitialized() {
        if (THREAD_LOCAL.get() == null) {
            THREAD_LOCAL.set(new HashMap<>());
        }
    }

    // 设置值
    public static void set(String key, Object value) {
        ensureInitialized();
        THREAD_LOCAL.get().put(key, value);
    }

    // 获取值
    public static Object get(String key) {
        ensureInitialized();
        return THREAD_LOCAL.get().get(key);
    }

    // 移除指定键
    public static void remove(String key) {
        if (THREAD_LOCAL.get() != null) {
            THREAD_LOCAL.get().remove(key);
        }
    }

    // 清理整个 ThreadLocal
    public static void clear() {
        THREAD_LOCAL.remove();
    }
}
