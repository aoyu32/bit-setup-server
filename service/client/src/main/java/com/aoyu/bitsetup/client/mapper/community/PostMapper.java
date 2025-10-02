package com.aoyu.bitsetup.client.mapper.community;

import com.aoyu.bitsetup.model.dto.community.PostDetailDTO;
import com.aoyu.bitsetup.model.dto.community.PostHotDTO;
import com.aoyu.bitsetup.model.dto.community.PostInfoDTO;
import com.aoyu.bitsetup.model.dto.community.PostRecommendDTO;
import com.aoyu.bitsetup.model.entity.community.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @InterfaceName：PostMapper
 * @Author: aoyu
 * @Date: 2025/10/1 下午2:17
 * @Description:
 */


@Mapper
public interface PostMapper extends BaseMapper<Post> {
    /**
     * 根据分类查询帖子列表
     * 如果 categoryId 为 null 或 0，则查询所有帖子
     *
     * @param categoryId 分类ID
     * @return 帖子信息列表
     */
    List<PostInfoDTO> selectPostListByCategory(@Param("categoryId") Long categoryId);

    PostDetailDTO selectPostDetailByPostId(Long postId);

    List<PostHotDTO> selectHotPost();

    List<PostRecommendDTO> selectRecommendPost();

}
