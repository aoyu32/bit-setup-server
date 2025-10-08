package com.aoyu.bitsetup.client.service.comment.impl;

import com.aoyu.bitsetup.client.mapper.comment.AppCommentImageMapper;
import com.aoyu.bitsetup.client.mapper.comment.AppCommentLikeMapper;
import com.aoyu.bitsetup.client.mapper.comment.AppCommentMapper;
import com.aoyu.bitsetup.client.mapper.user.UserInfoMapper;
import com.aoyu.bitsetup.client.service.comment.AppCommentService;
import com.aoyu.bitsetup.client.service.user.UserInfoService;
import com.aoyu.bitsetup.common.utils.MinioUtil;
import com.aoyu.bitsetup.model.dto.user.UserBaseInfoDTO;
import com.aoyu.bitsetup.model.entity.comment.AppComment;
import com.aoyu.bitsetup.model.entity.comment.AppCommentImage;
import com.aoyu.bitsetup.model.entity.comment.AppCommentLike;
import com.aoyu.bitsetup.model.vo.comment.CommentAddReqVO;
import com.aoyu.bitsetup.model.vo.comment.CommentReplyRespVO;
import com.aoyu.bitsetup.model.vo.comment.CommentRespVO;
import com.aoyu.bitsetup.model.entity.user.UserInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName：AppCommentServiceImpl
 * @Author: aoyu
 * @Date: 2025-10-07 00:29
 * @Description: 应用评论实现类
 */
@Service
@Slf4j
public class AppCommentServiceImpl implements AppCommentService {

    @Autowired
    private AppCommentMapper appCommentMapper;

    @Autowired
    private AppCommentImageMapper appCommentImageMapper;

    @Autowired
    private AppCommentLikeMapper appCommentLikeMapper;

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    private UserInfoService userInfoService;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addComment(CommentAddReqVO vo, Long uid) {
        // 创建评论实体
        AppComment comment = new AppComment();
        comment.setAppId(vo.getTargetId());
        comment.setUid(uid);
        comment.setContent(vo.getContent());
        comment.setParentId(vo.getParentId() == null ? 0L : vo.getParentId());
        comment.setRootId(vo.getRootId() == null ? 0L : vo.getRootId());
        comment.setStatus(2);
        comment.setIsTop(0);
        comment.setEditCount(0);

        // 保存评论
        appCommentMapper.insert(comment);

        // 保存图片
        if (!CollectionUtils.isEmpty(vo.getImageUrls())) {
            saveCommentImages(comment.getId(), vo.getImageUrls());
        }

        log.info("用户{}发表应用评论成功，评论ID：{}", uid, comment.getId());
        return comment.getId();

    }

    /**
     * 保存评论图片
     */
    public void saveCommentImages(Long commentId, List<String> imageUrls) {
        for (int i = 0; i < imageUrls.size(); i++) {
            AppCommentImage image = new AppCommentImage();
            image.setCommentId(commentId);
            image.setImageUrl(imageUrls.get(i));
            image.setSortOrder(i);
            image.setIsDeleted(0);
            appCommentImageMapper.insert(image);
        }
    }

    @Override
    public String uploadImage(MultipartFile multipartFile, String uid) {
        String path = "comment/app/" + uid;
        return minioUtil.uploadFileToFolder(multipartFile, path, true);
    }


    @Override
    public void deleteComment(Long commentId, Long uid) {

    }
    @Override
    public List<CommentRespVO> getComments(Long appId, Long currentUid) {
        // 1. 查询根评论（parent_id为null的评论）
        LambdaQueryWrapper<AppComment> rootWrapper = new LambdaQueryWrapper<>();
        rootWrapper.eq(AppComment::getAppId, appId)
                .eq(AppComment::getStatus, 2) // 审核通过的评论
                .eq(AppComment::getParentId,0) // 根评论
                .orderByDesc(AppComment::getIsTop) // 置顶优先
                .orderByDesc(AppComment::getCreateTime); // 时间倒序

        List<AppComment> rootComments = appCommentMapper.selectList(rootWrapper);
        if (CollectionUtils.isEmpty(rootComments)) {
            return new ArrayList<>();
        }

        // 2. 构建评论响应列表
        return buildCommentRespVOList(rootComments, currentUid);
    }

    private List<CommentRespVO> buildCommentRespVOList(List<AppComment> comments, Long currentUid) {
        List<CommentRespVO> result = new ArrayList<>();

        // 批量获取用户信息
        Set<Long> userIds = comments.stream()
                .map(AppComment::getUid)
                .collect(Collectors.toSet());
        Map<Long, UserBaseInfoDTO> userInfoMap = userInfoService.getUserBaseInfoMap(userIds);

        // 批量获取评论图片
        List<Long> commentIds = comments.stream()
                .map(AppComment::getId)
                .collect(Collectors.toList());
        Map<Long, List<String>> commentImagesMap = getCommentImagesMap(commentIds);

        // 批量获取点赞信息
        Map<Long, Integer> likeCountMap = getLikeCountMap(commentIds);
        Map<Long, Boolean> userLikeStatusMap = currentUid != null ?
                getUserLikeStatusMap(commentIds, currentUid) : new HashMap<>();

        // 批量获取回复数量
        Map<Long, Integer> replyCountMap = getReplyCountMap(commentIds);

        // 构建根评论VO
        for (AppComment comment : comments) {
            CommentRespVO vo = convertToCommentRespVO(comment, userInfoMap,
                    commentImagesMap, likeCountMap, userLikeStatusMap, replyCountMap);

            // 获取该评论的所有回复
            List<CommentReplyRespVO> replies = getCommentReplies(comment.getId(), currentUid);
            vo.setReplies(replies);

            result.add(vo);
        }

        return result;
    }

    private List<CommentReplyRespVO> getCommentReplies(Long rootCommentId, Long currentUid) {
        // 查询所有回复（parent_id不为null且root_id等于根评论ID）
        LambdaQueryWrapper<AppComment> replyWrapper = new LambdaQueryWrapper<>();
        replyWrapper.eq(AppComment::getRootId, rootCommentId)
                .eq(AppComment::getStatus, 2)
                .isNotNull(AppComment::getParentId)
                .orderByAsc(AppComment::getCreateTime); // 回复按时间正序排列

        List<AppComment> replies = appCommentMapper.selectList(replyWrapper);
        if (CollectionUtils.isEmpty(replies)) {
            return new ArrayList<>();
        }

        // 批量获取用户信息
        Set<Long> userIds = replies.stream()
                .map(AppComment::getUid)
                .collect(Collectors.toSet());
        // 还需要获取被回复用户的ID
        Set<Long> parentCommentIds = replies.stream()
                .map(AppComment::getParentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (!parentCommentIds.isEmpty()) {
            List<AppComment> parentComments = appCommentMapper.selectBatchIds(parentCommentIds);
            userIds.addAll(parentComments.stream()
                    .map(AppComment::getUid)
                    .collect(Collectors.toSet()));
        }
        Map<Long, UserBaseInfoDTO> userInfoMap = userInfoService.getUserBaseInfoMap(userIds);

        // 批量获取其他数据
        List<Long> replyIds = replies.stream()
                .map(AppComment::getId)
                .collect(Collectors.toList());
        Map<Long, List<String>> commentImagesMap = getCommentImagesMap(replyIds);
        Map<Long, Integer> likeCountMap = getLikeCountMap(replyIds);
        Map<Long, Boolean> userLikeStatusMap = currentUid != null ?
                getUserLikeStatusMap(replyIds, currentUid) : new HashMap<>();

        // 构建回复VO列表
        List<CommentReplyRespVO> replyVOs = new ArrayList<>();
        for (AppComment reply : replies) {
            CommentReplyRespVO replyVO = convertToCommentReplyRespVO(reply, userInfoMap,
                    commentImagesMap, likeCountMap, userLikeStatusMap);
            replyVOs.add(replyVO);
        }

        return replyVOs;
    }

    private CommentRespVO convertToCommentRespVO(AppComment comment,
                                                 Map<Long, UserBaseInfoDTO> userInfoMap,
                                                 Map<Long, List<String>> commentImagesMap,
                                                 Map<Long, Integer> likeCountMap,
                                                 Map<Long, Boolean> userLikeStatusMap,
                                                 Map<Long, Integer> replyCountMap) {
        CommentRespVO vo = new CommentRespVO();

        vo.setCid(comment.getId());

        vo.setIsAuthor(Boolean.FALSE);

        // 设置用户信息
        vo.setUser(userInfoMap.get(comment.getUid()));

        // 设置评论内容
        vo.setContent(comment.getContent());
        vo.setImages(commentImagesMap.getOrDefault(comment.getId(), new ArrayList<>()));

        // 设置互动数据
        vo.setLikeCount(likeCountMap.getOrDefault(comment.getId(), 0));
        vo.setIsLiked(userLikeStatusMap.getOrDefault(comment.getId(), false));
        vo.setReplyCount(replyCountMap.getOrDefault(comment.getId(), 0));

        // 设置其他信息
        vo.setIsTop(comment.getIsTop() == 1);
        vo.setCreateTime(comment.getCreateTime());
        vo.setEditTime(comment.getEditTime());

        return vo;
    }

    private CommentReplyRespVO convertToCommentReplyRespVO(AppComment reply,
                                                           Map<Long, UserBaseInfoDTO> userInfoMap,
                                                           Map<Long, List<String>> commentImagesMap,
                                                           Map<Long, Integer> likeCountMap,
                                                           Map<Long, Boolean> userLikeStatusMap) {
        CommentReplyRespVO vo = new CommentReplyRespVO();

        // 设置评论ID
        vo.setCid(reply.getId());

        vo.setIsAuthor(Boolean.FALSE); // 应用评论默认返回false

        // 设置用户信息
        vo.setUser(userInfoMap.get(reply.getUid()));

        // 设置评论内容
        vo.setContent(reply.getContent());
        vo.setImages(commentImagesMap.getOrDefault(reply.getId(), new ArrayList<>()));

        // 设置互动数据
        vo.setLikeCount(likeCountMap.getOrDefault(reply.getId(), 0));
        vo.setIsLiked(userLikeStatusMap.getOrDefault(reply.getId(), false));

        // 设置回复关系
        vo.setParentId(reply.getParentId());
        if (reply.getParentId() != null) {
            // 获取父评论的用户信息
            AppComment parentComment = appCommentMapper.selectById(reply.getParentId());
            if (parentComment != null) {
                vo.setReplyToUid(parentComment.getUid());
                UserBaseInfoDTO replyToUser = userInfoMap.get(parentComment.getUid());
                if (replyToUser != null) {
                    vo.setReplyToNickname(replyToUser.getNickname());
                }
            }
        }

        // 设置时间信息
        vo.setCreateTime(reply.getCreateTime());
        vo.setEditTime(reply.getEditTime());

        return vo;
    }

    private Map<Long, List<String>> getCommentImagesMap(List<Long> commentIds) {
        if (CollectionUtils.isEmpty(commentIds)) {
            return new HashMap<>();
        }

        LambdaQueryWrapper<AppCommentImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(AppCommentImage::getCommentId, commentIds)
                .eq(AppCommentImage::getIsDeleted, 0)
                .orderByAsc(AppCommentImage::getSortOrder);

        List<AppCommentImage> images = appCommentImageMapper.selectList(wrapper);

        return images.stream()
                .collect(Collectors.groupingBy(
                        AppCommentImage::getCommentId,
                        Collectors.mapping(AppCommentImage::getImageUrl, Collectors.toList())
                ));
    }

    private Map<Long, Integer> getLikeCountMap(List<Long> commentIds) {
        if (CollectionUtils.isEmpty(commentIds)) {
            return new HashMap<>();
        }

        // 使用SQL批量查询点赞数量
        return appCommentLikeMapper.selectLikeCountByCommentIds(commentIds);
    }

    private Map<Long, Boolean> getUserLikeStatusMap(List<Long> commentIds, Long currentUid) {
        if (CollectionUtils.isEmpty(commentIds) || currentUid == null) {
            return new HashMap<>();
        }

        LambdaQueryWrapper<AppCommentLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(AppCommentLike::getCommentId, commentIds)
                .eq(AppCommentLike::getUid, currentUid)
                .eq(AppCommentLike::getIsActive, 1);

        List<AppCommentLike> userLikes = appCommentLikeMapper.selectList(wrapper);

        return userLikes.stream()
                .collect(Collectors.toMap(
                        AppCommentLike::getCommentId,
                        like -> true
                ));
    }

    private Map<Long, Integer> getReplyCountMap(List<Long> commentIds) {
        if (CollectionUtils.isEmpty(commentIds)) {
            return new HashMap<>();
        }

        // 查询每个根评论的回复数量
        LambdaQueryWrapper<AppComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(AppComment::getRootId, commentIds)
                .eq(AppComment::getStatus, 2)
                .isNotNull(AppComment::getParentId);

        List<AppComment> replies = appCommentMapper.selectList(wrapper);

        return replies.stream()
                .collect(Collectors.groupingBy(
                        AppComment::getRootId,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));
    }
}
