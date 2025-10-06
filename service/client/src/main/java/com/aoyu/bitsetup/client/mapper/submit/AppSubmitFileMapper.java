package com.aoyu.bitsetup.client.mapper.submit;

import com.aoyu.bitsetup.model.entity.submit.AppSubmitFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @InterfaceName：AppSubmitFileMapper
 * @Author: aoyu
 * @Date: 2025/10/5 下午1:47
 * @Description:
 */

@Mapper
public interface AppSubmitFileMapper extends BaseMapper<AppSubmitFile> {
    /**
     * 根据投稿ID删除文件记录
     * @param submissionId 投稿ID
     * @return 删除的记录数
     */
    @Delete("DELETE FROM app_submit_file WHERE submission_id = #{submissionId}")
    int deleteFilesBySubmissionId(@Param("submissionId") Long submissionId);
}
