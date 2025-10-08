package com.aoyu.bitsetup.client.mapper.comment;

import com.aoyu.bitsetup.model.entity.comment.AppCommentLike;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @ClassName：AppCommentLikeMapper
 * @Author: aoyu
 * @Date: 2025-10-07 00:27
 * @Description: 评论点赞记录Mapper
 */
@Mapper
public interface AppCommentLikeMapper extends BaseMapper<AppCommentLike> {
    @Select("<script>" +
            "SELECT comment_id, COUNT(*) as like_count " +
            "FROM app_comment_like " +
            "WHERE comment_id IN " +
            "<foreach collection='commentIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            " AND is_active = 1 " +
            "GROUP BY comment_id" +
            "</script>")
    @MapKey("comment_id")
    Map<Long, Integer> selectLikeCountByCommentIds(@Param("commentIds") List<Long> commentIds);
}