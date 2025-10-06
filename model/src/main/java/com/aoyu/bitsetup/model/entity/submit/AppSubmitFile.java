package com.aoyu.bitsetup.model.entity.submit;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.sql.Timestamp;

/**
 * @ClassName：AppSubmitFile
 * @Author: aoyu
 * @Date: 2025-10-05 13:40
 * @Description: 应用提交文件实体
 */
@Data
@TableName("app_submit_file")
public class AppSubmitFile {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id; // 雪花算法ID（主键）

    private Long submissionId; // 投稿ID
    private String fileName; // 文件名
    private String fileType; // 文件类型
    private String fileUrl; // 文件下载地址
    private Long fileSize; // 文件大小(字节)
    private Boolean isDeleted; // 是否逻辑删除：0-否，1-是
    private Timestamp deleteTime; // 删除时间
    private Timestamp createTime; // 创建时间
    private Timestamp updateTime; // 更新时间
}
