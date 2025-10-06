package com.aoyu.bitsetup.model.entity.submit;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.sql.Timestamp;

/**
 * @ClassName：AppSubmitScreenshot
 * @Author: aoyu
 * @Date: 2025-10-05 13:41
 * @Description: 应用提交截图实体
 */
@Data
@TableName("app_submit_screenshot")
public class AppSubmitScreenshot {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id; // 雪花算法ID（主键）

    private Long submissionId; // 投稿ID
    private String imageUrl; // 截图URL
    private String imageFormat; // 图片格式
    private Integer sortOrder; // 排序序号
    private Integer imageSize; // 图片大小(字节)
    private Integer imageWidth; // 图片宽度
    private Integer imageHeight; // 图片高度
    private Boolean isDeleted; // 是否逻辑删除：0-否，1-是
    private Timestamp deleteTime; // 删除时间
    private Timestamp createTime; // 创建时间
    private Timestamp updateTime; // 更新时间
}
