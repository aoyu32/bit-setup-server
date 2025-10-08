package com.aoyu.bitsetup.client.mapper.comment;

import com.aoyu.bitsetup.model.entity.comment.AppComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName：AppCommentMapper
 * @Author: aoyu
 * @Date: 2025-10-07 00:26
 * @Description: 应用评论Mapper
 */
@Mapper
public interface AppCommentMapper extends BaseMapper<AppComment> {
}