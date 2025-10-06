package com.aoyu.bitsetup.model.entity.user;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
/**
 * @ClassName：UserSearchHistory
 * @Author: aoyu
 * @Date: 2025-10-06 15:15
 * @Description: 用户搜索历史实体
 */



@Data
@TableName("user_search_history")
public class UserSearchHistory {


    /**
     * 记录id（雪花ID）
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id（与user_info表uid关联）
     */
    private Long uid;

    /**
     * 搜索关键词
     */
    private String keyword;

    /**
     * 搜索类型：app-应用搜索，category-分类搜索，developer-开发者搜索，general-综合搜索
     */
    private String searchType;

    /**
     * 搜索结果数量
     */
    private Integer resultCount;

    /**
     * 搜索来源：web-网页端，mobile-移动端，api-API接口
     */
    private String searchSource;

    /**
     * 搜索IP地址
     */
    private String ipAddress;

    /**
     * 用户代理信息
     */
    private String userAgent;

    /**
     * 是否删除：0-否，1-是
     */
    private Integer isDeleted;

    /**
     * 搜索时间
     */
    private Timestamp createTime;

    /**
     * 更新时间
     */
    private Timestamp updateTime;
}
