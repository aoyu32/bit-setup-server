package com.aoyu.bitsetup.client.mapper.submit;

import com.aoyu.bitsetup.model.dto.submit.DevelopSubmitDraftDTO;
import com.aoyu.bitsetup.model.dto.submit.RecommendSubmitDraftDTO;
import com.aoyu.bitsetup.model.dto.submit.SubmitDraftDTO;
import com.aoyu.bitsetup.model.entity.submit.AppSubmit;
import com.aoyu.bitsetup.model.entity.submit.AppSubmitScreenshot;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @InterfaceName：AppSubmitMapper
 * @Author: aoyu
 * @Date: 2025/10/5 下午1:47
 * @Description:
 */

@Mapper
public interface AppSubmitMapper extends BaseMapper<AppSubmit> {

    @Insert({
            "<script>",
            "INSERT INTO app_submit_screenshot (id, submission_id, image_url) VALUES ",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.id}, #{item.submissionId}, #{item.imageUrl})",
            "</foreach>",
            "</script>"
    })
    int insertBatch(@Param("list") List<AppSubmitScreenshot> list);

    @Update({
            "<script>",
            "<foreach collection='list' item='item' separator=';'>",
            "UPDATE app_submit_screenshot ",
            "SET image_url = #{item.imageUrl} ",
            "WHERE id = #{item.id} AND submission_id = #{item.submissionId}",
            "</foreach>",
            "</script>"
    })
    int updateBatch(@Param("list") List<AppSubmitScreenshot> list);

    DevelopSubmitDraftDTO selectDevelopSubmit(Long uid);
    RecommendSubmitDraftDTO selectRecommendSubmit(Long uid);


    int deleteScreenshots(@Param("submissionId") Long submissionId);
}
