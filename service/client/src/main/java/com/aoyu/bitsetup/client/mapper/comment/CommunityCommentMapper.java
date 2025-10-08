package com.aoyu.bitsetup.client.mapper.comment;

import com.aoyu.bitsetup.model.entity.comment.CommunityComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName：CommunityCommentMapper
 * @Author: aoyu
 * @Date: 2025-10-07 00:27
 * @Description: 帖子评论Mapper
 */
@Mapper
public interface CommunityCommentMapper extends BaseMapper<CommunityComment> {
}