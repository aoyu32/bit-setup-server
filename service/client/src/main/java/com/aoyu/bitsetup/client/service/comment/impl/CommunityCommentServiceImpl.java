package com.aoyu.bitsetup.client.service.comment.impl;

import com.aoyu.bitsetup.client.mapper.comment.CommunityCommentImageMapper;
import com.aoyu.bitsetup.client.mapper.comment.CommunityCommentLikeMapper;
import com.aoyu.bitsetup.client.mapper.comment.CommunityCommentMapper;
import com.aoyu.bitsetup.client.service.comment.CommunityCommentService;
import com.aoyu.bitsetup.client.service.user.UserInfoService;
import com.aoyu.bitsetup.common.utils.MinioUtil;
import com.aoyu.bitsetup.model.dto.user.UserBaseInfoDTO;
import com.aoyu.bitsetup.model.entity.comment.CommunityComment;
import com.aoyu.bitsetup.model.entity.comment.CommunityCommentImage;
import com.aoyu.bitsetup.model.entity.comment.CommunityCommentLike;
import com.aoyu.bitsetup.model.vo.comment.CommentAddReqVO;
import com.aoyu.bitsetup.model.vo.comment.CommentReplyRespVO;
import com.aoyu.bitsetup.model.vo.comment.CommentRespVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName：CommunityCommentServiceImpl
 * @Author: aoyu
 * @Date: 2025-10-07 00:30
 * @Description: 社区评论接口实现类
 */

@Service
@Slf4j
public class CommunityCommentServiceImpl implements CommunityCommentService {

    @Autowired
    private CommunityCommentMapper communityCommentMapper;

    @Autowired
    private CommunityCommentImageMapper communityCommentImageMapper;

    @Autowired
    private CommunityCommentLikeMapper communityCommentLikeMapper;

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    private UserInfoService userInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addComment(CommentAddReqVO vo, Long uid) {
        // 创建评论实体
        CommunityComment comment = new CommunityComment();
        comment.setPostId(vo.getTargetId());
        comment.setUid(uid);
        comment.setContent(vo.getContent());
        comment.setParentId(vo.getParentId() == null ? 0L : vo.getParentId());

        // 设置回复的用户uid（如果是回复评论）
        if (vo.getParentId() != null && vo.getParentId() > 0) {
            CommunityComment parentComment = communityCommentMapper.selectById(vo.getParentId());
            if (parentComment != null) {
                comment.setReplyUid(parentComment.getUid());
            }
        }

        comment.setStatus(1); // 1-正常

        // 保存评论
        communityCommentMapper.insert(comment);

        // 保存图片
        if (!CollectionUtils.isEmpty(vo.getImageUrls())) {
            saveCommentImages(comment.getId(), vo.getImageUrls());
        }

        log.info("用户{}发表社区评论成功，评论ID：{}", uid, comment.getId());
        return comment.getId();
    }

    /**
     * 保存评论图片
     */
    private void saveCommentImages(Long commentId, List<String> imageUrls) {
        for (int i = 0; i < imageUrls.size(); i++) {
            CommunityCommentImage image = new CommunityCommentImage();
            image.setCommentId(commentId);
            image.setImageUrl(imageUrls.get(i));
            image.setSortOrder(i);
            image.setIsDeleted(0);
            communityCommentImageMapper.insert(image);
        }
    }

    @Override
    public String uploadImage(MultipartFile multipartFile, String uid) {
        String path = "comment/community/" + uid;
        return minioUtil.uploadFileToFolder(multipartFile, path, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long commentId, Long uid) {
        CommunityComment comment = communityCommentMapper.selectById(commentId);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }

        if (!comment.getUid().equals(uid)) {
            throw new RuntimeException("无权删除该评论");
        }

        // 软删除：将状态设置为0
        comment.setStatus(0);
        communityCommentMapper.updateById(comment);

        log.info("用户{}删除评论{}成功", uid, commentId);
    }

    @Override
    public List<CommentRespVO> getComments(Long postId, Long currentUid) {
        // 1. 查询该帖子的所有评论（包括顶级评论和所有回复）
        LambdaQueryWrapper<CommunityComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityComment::getPostId, postId)
                .eq(CommunityComment::getStatus, 1)
                .orderByAsc(CommunityComment::getCreateTime); // 按时间正序

        List<CommunityComment> allComments = communityCommentMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(allComments)) {
            return new ArrayList<>();
        }

        // 2. 分离顶级评论和回复评论
        List<CommunityComment> rootComments = allComments.stream()
                .filter(c -> c.getParentId() == 0)
                .sorted(Comparator.comparing(CommunityComment::getCreateTime).reversed()) // 顶级评论按时间倒序
                .collect(Collectors.toList());

        Map<Long, List<CommunityComment>> replyMap = allComments.stream()
                .filter(c -> c.getParentId() != 0)
                .collect(Collectors.groupingBy(CommunityComment::getParentId));

        // 3. 准备所有需要的数据
        Set<Long> allUserIds = allComments.stream()
                .map(CommunityComment::getUid)
                .collect(Collectors.toSet());
        allUserIds.addAll(allComments.stream()
                .map(CommunityComment::getReplyUid)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()));
        Map<Long, UserBaseInfoDTO> userInfoMap = userInfoService.getUserBaseInfoMap(allUserIds);

        List<Long> allCommentIds = allComments.stream()
                .map(CommunityComment::getId)
                .collect(Collectors.toList());
        Map<Long, List<String>> commentImagesMap = getCommentImagesMap(allCommentIds);
        Map<Long, Integer> likeCountMap = getLikeCountMap(allCommentIds);
        Map<Long, Boolean> userLikeStatusMap = currentUid != null ?
                getUserLikeStatusMap(allCommentIds, currentUid) : new HashMap<>();

        // 4. 构建返回结果
        List<CommentRespVO> result = new ArrayList<>();
        for (CommunityComment rootComment : rootComments) {
            CommentRespVO vo = convertToCommentRespVO(rootComment, userInfoMap,
                    commentImagesMap, likeCountMap, userLikeStatusMap);

            // 递归构建该顶级评论下的所有回复
            List<CommentReplyRespVO> replies = buildReplyTree(rootComment.getId(), replyMap,
                    userInfoMap, commentImagesMap, likeCountMap, userLikeStatusMap);
            vo.setReplies(replies);
            vo.setReplyCount(replies.size()); // 设置实际的回复数量

            result.add(vo);
        }

        return result;
    }

    /**
     * 递归构建回复树
     */
    private List<CommentReplyRespVO> buildReplyTree(Long parentId,
                                                    Map<Long, List<CommunityComment>> replyMap,
                                                    Map<Long, UserBaseInfoDTO> userInfoMap,
                                                    Map<Long, List<String>> commentImagesMap,
                                                    Map<Long, Integer> likeCountMap,
                                                    Map<Long, Boolean> userLikeStatusMap) {
        List<CommunityComment> directReplies = replyMap.get(parentId);
        if (CollectionUtils.isEmpty(directReplies)) {
            return new ArrayList<>();
        }

        List<CommentReplyRespVO> result = new ArrayList<>();
        for (CommunityComment reply : directReplies) {
            CommentReplyRespVO replyVO = convertToCommentReplyRespVO(reply, userInfoMap,
                    commentImagesMap, likeCountMap, userLikeStatusMap);

            // 递归获取子回复
            List<CommentReplyRespVO> childReplies = buildReplyTree(reply.getId(), replyMap,
                    userInfoMap, commentImagesMap, likeCountMap, userLikeStatusMap);

            // 将所有子回复添加到结果中（扁平化处理）
            result.add(replyVO);
            result.addAll(childReplies);
        }

        return result;
    }

    private CommentRespVO convertToCommentRespVO(CommunityComment comment,
                                                 Map<Long, UserBaseInfoDTO> userInfoMap,
                                                 Map<Long, List<String>> commentImagesMap,
                                                 Map<Long, Integer> likeCountMap,
                                                 Map<Long, Boolean> userLikeStatusMap) {
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
        vo.setReplyCount(0); // 先设置为0，后面会更新

        // 设置其他信息
        vo.setIsTop(false); // 社区评论默认不置顶
        vo.setCreateTime(comment.getCreateTime());
        vo.setEditTime(comment.getUpdateTime());

        return vo;
    }

    private CommentReplyRespVO convertToCommentReplyRespVO(CommunityComment reply,
                                                           Map<Long, UserBaseInfoDTO> userInfoMap,
                                                           Map<Long, List<String>> commentImagesMap,
                                                           Map<Long, Integer> likeCountMap,
                                                           Map<Long, Boolean> userLikeStatusMap) {
        CommentReplyRespVO vo = new CommentReplyRespVO();

        // 设置评论ID
        vo.setCid(reply.getId());
        vo.setIsAuthor(Boolean.FALSE);

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
        if (reply.getReplyUid() != null) {
            vo.setReplyToUid(reply.getReplyUid());
            UserBaseInfoDTO replyToUser = userInfoMap.get(reply.getReplyUid());
            if (replyToUser != null) {
                vo.setReplyToNickname(replyToUser.getNickname());
            }
        }

        // 设置时间信息
        vo.setCreateTime(reply.getCreateTime());
        vo.setEditTime(reply.getUpdateTime());

        return vo;
    }

    private Map<Long, List<String>> getCommentImagesMap(List<Long> commentIds) {
        if (CollectionUtils.isEmpty(commentIds)) {
            return new HashMap<>();
        }

        LambdaQueryWrapper<CommunityCommentImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CommunityCommentImage::getCommentId, commentIds)
                .eq(CommunityCommentImage::getIsDeleted, 0)
                .orderByAsc(CommunityCommentImage::getSortOrder);

        List<CommunityCommentImage> images = communityCommentImageMapper.selectList(wrapper);

        return images.stream()
                .collect(Collectors.groupingBy(
                        CommunityCommentImage::getCommentId,
                        Collectors.mapping(CommunityCommentImage::getImageUrl, Collectors.toList())
                ));
    }

    private Map<Long, Integer> getLikeCountMap(List<Long> commentIds) {
        if (CollectionUtils.isEmpty(commentIds)) {
            return new HashMap<>();
        }

        // 使用SQL批量查询点赞数量
        return communityCommentLikeMapper.selectLikeCountByCommentIds(commentIds);
    }

    private Map<Long, Boolean> getUserLikeStatusMap(List<Long> commentIds, Long currentUid) {
        if (CollectionUtils.isEmpty(commentIds) || currentUid == null) {
            return new HashMap<>();
        }

        LambdaQueryWrapper<CommunityCommentLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CommunityCommentLike::getCommentId, commentIds)
                .eq(CommunityCommentLike::getUid, currentUid)
                .eq(CommunityCommentLike::getStatus, 1);

        List<CommunityCommentLike> userLikes = communityCommentLikeMapper.selectList(wrapper);

        return userLikes.stream()
                .collect(Collectors.toMap(
                        CommunityCommentLike::getCommentId,
                        like -> true
                ));
    }
}
