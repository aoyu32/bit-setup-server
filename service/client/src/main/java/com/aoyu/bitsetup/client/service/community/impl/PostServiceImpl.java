package com.aoyu.bitsetup.client.service.community.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.aoyu.bitsetup.client.mapper.community.PostCategoryMapper;
import com.aoyu.bitsetup.client.mapper.community.PostImageMapper;
import com.aoyu.bitsetup.client.mapper.community.PostMapper;
import com.aoyu.bitsetup.client.mapper.user.UserInfoMapper;
import com.aoyu.bitsetup.client.service.community.PostService;
import com.aoyu.bitsetup.common.exception.BusinessException;
import com.aoyu.bitsetup.common.utils.MinioUtil;
import com.aoyu.bitsetup.model.dto.community.PostDetailDTO;
import com.aoyu.bitsetup.model.dto.community.PostHotDTO;
import com.aoyu.bitsetup.model.dto.community.PostInfoDTO;
import com.aoyu.bitsetup.model.dto.community.PostRecommendDTO;
import com.aoyu.bitsetup.model.dto.user.UserDetailInfoDTO;
import com.aoyu.bitsetup.model.dto.user.UserInfoDTO;
import com.aoyu.bitsetup.model.entity.community.Post;
import com.aoyu.bitsetup.model.entity.community.PostCategory;
import com.aoyu.bitsetup.model.entity.community.PostImage;
import com.aoyu.bitsetup.model.vo.community.PostDetailRespVO;
import com.aoyu.bitsetup.model.vo.community.PostPutReqVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName：PostServiceImpl
 * @Author: aoyu
 * @Date: 2025-10-01 14:19
 * @Description: 帖子接口实现类
 */

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostCategoryMapper postCategoryMapper;

    @Autowired
    private PostImageMapper postImageMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;


    @Override
    public void savePost(PostPutReqVO postPutReqVO) {

        log.info("发帖请求参数{}", postPutReqVO.toString());
        //获取分类id
        LambdaQueryWrapper<PostCategory> postCategoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        postCategoryLambdaQueryWrapper
                .select(PostCategory::getId)
                .eq(PostCategory::getCode, postPutReqVO.getCategory())
                .eq(PostCategory::getStatus, 1);
        PostCategory postCategory = postCategoryMapper.selectOne(postCategoryLambdaQueryWrapper);
        if (postCategory == null) {
            throw new BusinessException(600, "未知分类");
        }

        Post post = new Post();

        post.setUid(postPutReqVO.getUid());
        post.setCategoryId(postCategory.getId());
        post.setTitle(postPutReqVO.getTitle());
        post.setPublishTime(new Date());
        post.setSummary(postPutReqVO.getSummary());
        post.setContent(postPutReqVO.getContent());

        if (!postPutReqVO.getIsPublish()) {
            post.setStatus(0);
        }

        //插入数据
        int insert = postMapper.insert(post);
        if (insert == 0) {
            throw new BusinessException(601, "帖子数据报错错误");
        }

        if (!postPutReqVO.getImages().isEmpty()) {

            List<String> imageUrls = postPutReqVO.getImages();

            for (int i = 0; i < imageUrls.size(); i++) {
                PostImage postImage = new PostImage();
                postImage.setImageUrl(imageUrls.get(i));
                postImage.setUid(postPutReqVO.getUid());
                postImage.setPostId(post.getId());
                postImage.setSort(i+1); // 使用循环索引
                postImageMapper.insert(postImage);
            }

        }


    }

    @Override
    public String uploadPostImage(MultipartFile multipartFile, String uid) {
        String path = "community/post/" + uid;
        return minioUtil.uploadFileToFolder(multipartFile, path, true);
    }

    @Override
    public List<PostInfoDTO> getPostList(String category) {
        //获取分类id
        LambdaQueryWrapper<PostCategory> postCategoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        postCategoryLambdaQueryWrapper
                .select(PostCategory::getId)
                .eq(PostCategory::getCode, category)
                .eq(PostCategory::getStatus, 1);
        PostCategory postCategory = postCategoryMapper.selectOne(postCategoryLambdaQueryWrapper);
        List<PostInfoDTO> postInfoDTOList = new ArrayList<>();
        if (postCategory == null) {
            postInfoDTOList = postMapper.selectPostListByCategory(null);
        }else {

            //查询列表
            postInfoDTOList = postMapper.selectPostListByCategory(postCategory.getId());
        }

        return postInfoDTOList;
    }

    @Override
    public PostDetailRespVO getPostDetail(String postId) {

        //查询帖子信息
        PostDetailDTO postDetailDTO = postMapper.selectPostDetailByPostId(Long.valueOf(postId));
        //根据帖子的用户id查询用户信息
        UserInfoDTO userInfoDTO = userInfoMapper.selectBaseInfoById(postDetailDTO.getUid());
        PostDetailRespVO postDetailRespVO = new PostDetailRespVO();
        UserDetailInfoDTO userDetailInfoDTO = new UserDetailInfoDTO();
        BeanUtils.copyProperties(userInfoDTO, userDetailInfoDTO);
        postDetailRespVO.setUser(userDetailInfoDTO);
        postDetailRespVO.setPost(postDetailDTO);

        return postDetailRespVO;
    }

    @Override
    public List<PostHotDTO> getHotPostList() {
        return postMapper.selectHotPost();
    }

    @Override
    public List<PostRecommendDTO> getRecommendPostList() {
        return postMapper.selectRecommendPost();
    }
}
