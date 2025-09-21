package com.aoyu.bitsetup.model.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName：UserPoints
 * @Author: aoyu
 * @Date: 2025-09-21 10:36
 * @Description: 用户积分实体
 */

@Data
@TableName("user_points")
public class UserPoints {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id; // 记录id（雪花ID）

    private Long uid; // 用户id（与user_info表uid关联）

    private Integer totalPoints; // 总积分（历史累计）

    private Integer availablePoints; // 可用积分（当前余额）

    private Integer consumedPoints; // 已消费积分

    private Date createTime; // 创建时间

    private Date updateTime; // 更新时间

}
