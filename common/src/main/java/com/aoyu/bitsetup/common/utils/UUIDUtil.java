package com.aoyu.bitsetup.common.utils;

import java.util.UUID;

/**
 * @ClassName：UUIDUtil
 * @Author: aoyu
 * @Date: 2025-09-23 17:52
 * @Description: uuid和uid互相转换工具类
 */

public class UUIDUtil {

    /**
     * 将雪花ID转换为UUID字符串
     * 使用雪花ID作为种子生成看起来随机的UUID，但保持可逆性
     *
     * @param snowflakeId 雪花ID
     * @return 转换后的UUID字符串
     */
    public static String generateUUID(long snowflakeId) {
        return generateUUIDObject(snowflakeId).toString();
    }

    /**
     * 将雪花ID转换为UUID对象
     * 使用雪花ID作为种子生成看起来随机的UUID，但保持可逆性
     *
     * @param snowflakeId 雪花ID
     * @return 转换后的UUID对象
     */
    public static UUID generateUUIDObject(long snowflakeId) {
        // 使用简单的位运算和常数来"打散"雪花ID，让UUID看起来更随机
        long hash1 = snowflakeId ^ 0x123456789ABCDEFL;
        long hash2 = Long.rotateLeft(snowflakeId, 21) ^ 0xFEDCBA9876543210L;

        // 不修改版本位和变体位，保持原始数据用于验证
        return new UUID(hash1, hash2);
    }

    /**
     * 将UUID字符串逆向转换为雪花ID
     *
     * @param uuidString UUID字符串
     * @return 原始雪花ID
     */
    public static long parseUUID(String uuidString) {
        if (uuidString == null || uuidString.trim().isEmpty()) {
            throw new IllegalArgumentException("UUID字符串不能为空");
        }

        try {
            UUID uuid = UUID.fromString(uuidString);
            return parseUUID(uuid);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("无效的UUID格式: " + uuidString, e);
        }
    }

    /**
     * 将UUID对象逆向转换为雪花ID
     *
     * @param uuid 需要转换的UUID
     * @return 原始雪花ID
     */
    public static long parseUUID(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID不能为null");
        }

        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();

        // 逆向计算：恢复hash1和hash2
        long hash1 = mostSigBits;
        long hash2 = leastSigBits;

        // 从hash1逆向计算雪花ID
        long snowflakeId1 = hash1 ^ 0x123456789ABCDEFL;

        // 从hash2逆向计算雪花ID进行验证
        long rotatedSnowflake = hash2 ^ 0xFEDCBA9876543210L;
        long snowflakeId2 = Long.rotateRight(rotatedSnowflake, 21);

        // 简单验证：两种计算方式应该得到相同结果
        if (snowflakeId1 == snowflakeId2) {
            return snowflakeId1;
        } else {
            throw new IllegalArgumentException("UUID转换失败，可能不是由雪花ID生成");
        }
    }


}
