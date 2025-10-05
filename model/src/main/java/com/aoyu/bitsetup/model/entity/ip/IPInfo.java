package com.aoyu.bitsetup.model.entity.ip;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName：IPInfo
 * @Author: aoyu
 * @Date: 2025-10-03 23:09
 * @Description: ip信息实体
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IPInfo {

    /**
     * 查询的IP地址
     * 示例值："117.72.89.209"
     */
    private String ip;

    /**
     * 省份名称
     * 可能为空字符串（国外IP或未知地区）
     * 示例值："江苏省"
     */
    private String pro;

    /**
     * 省份编码
     * 999999表示国外或未知地区
     * 示例值："320000"（江苏）
     */
    private String proCode;

    /**
     * 城市名称
     * 可能为空字符串（国外IP或未知地区）
     * 示例值："南京市"
     */
    private String city;

    /**
     * 城市编码
     * 0表示未知
     * 示例值："320100"（南京）
     */
    private String cityCode;

    /**
     * 区/县名称
     * 通常为空字符串
     */
    private String region;

    /**
     * 区/县编码
     * 0表示未知
     */
    private String regionCode;

    /**
     * 完整地址描述
     * 包含国家、省份、城市和运营商信息
     * 国内示例："江苏省南京市 电信"
     * 国外示例："美国CloudFlare公司CDN节点"
     */
    private String addr;

    /**
     * 地址编码（非标准字段）
     * 某些版本API可能返回此字段
     */
    private String addrCode;

    /**
     * 区域名称（备用字段）
     * 通常为空字符串
     */
    private String regionNames;

    /**
     * 错误信息
     * 正常情况为空字符串
     * "noprovince"表示无法确定省份（常见于国外IP）
     * 其他可能值："未知IP"等
     */
    private String err;


}