package com.aoyu.bitsetup.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName：FileCategoryEnum
 * @Author: aoyu
 * @Date: 2025-10-01 20:40
 * @Description: 文件业务类型枚举
 */

@Getter
@AllArgsConstructor
public enum FileCategoryEnum {

    // 应用相关
    APP_ICON("app", "icons", "应用图标"),
    APP_INSTALLER("app", "resource", "应用安装包"),
    APP_SCREENSHOT("app", "screenshot", "应用截图"),
    APP_REVIEW_IMAGE("app", "review", "应用评论图片"),

    // 用户相关
    USER_AVATAR("user", "avatar", "用户头像"),
    USER_BACKGROUND("user", "background", "用户主页背景"),

    // 社区相关
    POST_PREVIEW("community", "preview", "帖子预览图"),
    POST_CONTENT("community", "content", "帖子内容图片"),
    ARTICLE_COVER("community", "cover", "文章封面"),
    ARTICLE_CONTENT("community", "content", "文章内容图片"),
    COMMENT_IMAGE("community", "comment", "评论图片"),

    // 临时文件
    TEMP_FILE("temp", "uploads", "临时文件");

    private final String businessType;
    private final String fileType;
    private final String description;

}
