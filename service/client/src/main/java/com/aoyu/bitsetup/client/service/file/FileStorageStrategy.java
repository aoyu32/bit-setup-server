package com.aoyu.bitsetup.client.service.file;

import com.aoyu.bitsetup.common.enumeration.FileCategoryEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @ClassName：FileStorageStrategy
 * @Author: aoyu
 * @Date: 2025-10-01 20:44
 * @Description: 文件存储策略类
 */

@Component
public class FileStorageStrategy {

    private static final String MAIN_BUCKET = "bit-setup";

    /**
     * 生成对象存储路径
     */
    public String generateObjectPath(FileCategoryEnum category, String resourceId, String originalFileName) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String fileExt = getFileExtension(originalFileName);
        String randomId = UUID.randomUUID().toString().substring(0, 8);
        String safeFileName = generateSafeFileName(originalFileName);

        return switch (category) {
            // 应用相关
            case APP_ICON ->
                    String.format("app/%s/icon/%s_%s.%s", resourceId, timestamp, randomId, fileExt);
            case APP_INSTALLER ->
                    String.format("app/%s/resource/%s_%s.%s", resourceId, timestamp, safeFileName, fileExt);
            case APP_SCREENSHOT ->
                    String.format("app/%s/screenshot/%s_%s.%s", resourceId, timestamp, randomId, fileExt);
            case APP_REVIEW_IMAGE ->
                    String.format("app/%s/review/%s_%s.%s", resourceId, timestamp, randomId, fileExt);

            // 用户相关
            case USER_AVATAR ->
                    String.format("user/%s/avatar/%s_%s.%s", resourceId, timestamp, randomId, fileExt);
            case USER_BACKGROUND ->
                    String.format("user/%s/background/%s_%s.%s", resourceId, timestamp, randomId, fileExt);

            // 社区相关
            case POST_PREVIEW ->
                    String.format("community/post/%s/previews/%s_%s.%s", resourceId, timestamp, randomId, fileExt);
            case POST_CONTENT ->
                    String.format("community/post/%s/contents/%s_%s.%s", resourceId, timestamp, randomId, fileExt);
            case ARTICLE_COVER ->
                    String.format("community/article/%s/covers/%s_%s.%s", resourceId, timestamp, randomId, fileExt);
            case ARTICLE_CONTENT ->
                    String.format("community/article/%s/contents/%s_%s.%s", resourceId, timestamp, randomId, fileExt);
            case COMMENT_IMAGE ->
                    String.format("community/comment/%s/%s_%s.%s", resourceId, timestamp, randomId, fileExt);

            // 临时文件
            case TEMP_FILE ->
                    String.format("temp/%s/%s_%s.%s", resourceId, timestamp, randomId, fileExt);
        };
    }

    /**
     * 获取存储桶名称
     */
    public String getBucketName(FileCategoryEnum category) {
        return MAIN_BUCKET;
    }

    /**
     * 获取文件访问URL前缀（用于CDN）
     */
    public String getUrlPrefix(FileCategoryEnum category) {
        return switch (category) {
            case APP_ICON, USER_AVATAR -> "https://cdn.example.com/images/";
            case APP_INSTALLER -> "https://download.example.com/";
            default -> "https://cdn.example.com/files/";
        };
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    private String generateSafeFileName(String fileName) {
        // 移除路径信息，只保留文件名
        String safeName = fileName.replaceAll(".*[/\\\\]", "");
        // 替换特殊字符
        return safeName.replaceAll("[^a-zA-Z0-9.-]", "_");
    }

}
