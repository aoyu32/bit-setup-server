package com.aoyu.bitsetup.model.entity.comment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * @ClassName：AppComment
 * @Author: aoyu
 * @Date: 2025-10-07 00:17
 * @Description: 应用评论实体
 */
@Data
@TableName("app_comment")
public class AppComment {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id; // 雪花算法ID

    private Long appId; // 应用ID

    private Long uid; // 用户ID

    private String content; // 评论内容

    private Long parentId; // 父评论ID

    private Long rootId; // 根评论ID

    private String ipAddress; // 评论IP地址

    private Integer status; // 状态：0-删除，1-待审核，2-审核通过，3-审核拒绝

    private Integer isTop; // 是否置顶

    private LocalDateTime createTime; // 创建时间

    private LocalDateTime updateTime; // 更新时间

    private LocalDateTime editTime; // 编辑时间

    private Integer editCount; // 编辑次数

}
