package com.aoyu.bitsetup.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortTypeEnum {

    COMPREHENSIVE(0, "id", false, "综合排序"),
    MOST_DOWNLOAD(1, "download_count", false, "最多下载"),
    MOST_COMMENT(2, "comment_count", false, "最多评论"),
    MOST_COLLECT(3, "collect_count", false, "最多收藏"),
    NEWEST(4, "create_time", false, "最新发布"),
    HIGHEST_RATING(5, "rating", false, "最高评分");

    private final int code;       // sortType 前端传的值
    private final String column;  // 数据库字段
    private final boolean asc;    // 默认是否升序
    private final String desc;    // 描述（可选，便于调试）

    public static SortTypeEnum fromCode(int code) {
        for (SortTypeEnum type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return COMPREHENSIVE; // 默认综合排序
    }
}