package com.aoyu.bitsetup.client.mapper.comment;

import com.aoyu.bitsetup.model.entity.comment.CommunityCommentLike;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @ClassName：CommunityCommentLikeMapper
 * @Author: aoyu
 * @Date: 2025-10-07 00:27
 * @Description: 评论点赞记录Mapper
 */
@Mapper
public interface CommunityCommentLikeMapper extends BaseMapper<CommunityCommentLike> {
    /**
     * 批量查询评论点赞数量
     *
     * @param commentIds 评论ID列表
     * @return 评论ID -> 点赞数量的映射
     */
    @Select("<script>" +
            "SELECT comment_id, COUNT(*) as count " +
            "FROM community_comment_like " +
            "WHERE comment_id IN " +
            "<foreach collection='commentIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            " AND status = 1 " +
            "GROUP BY comment_id" +
            "</script>")
    List<Map<String, Object>> selectLikeCountMapByCommentIds(@Param("commentIds") List<Long> commentIds);

    /**
     * 批量查询评论点赞数量（返回Map格式）
     *
     * @param commentIds 评论ID列表
     * @return 评论ID -> 点赞数量的映射
     */
    default Map<Long, Integer> selectLikeCountByCommentIds(List<Long> commentIds) {
        List<Map<String, Object>> results = selectLikeCountMapByCommentIds(commentIds);
        return results.stream()
                .collect(java.util.stream.Collectors.toMap(
                        map -> ((Number) map.get("comment_id")).longValue(),
                        map -> ((Number) map.get("count")).intValue()
                ));
    }
}