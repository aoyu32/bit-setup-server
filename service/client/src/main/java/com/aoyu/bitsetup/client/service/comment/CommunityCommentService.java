package com.aoyu.bitsetup.client.service.comment;

import com.aoyu.bitsetup.model.vo.comment.CommentAddReqVO;
import com.aoyu.bitsetup.model.vo.comment.CommentRespVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @InterfaceName：CommunityCommentService
 * @Author: aoyu
 * @Date: 2025/10/7 上午12:29
 * @Description:
 */

public interface CommunityCommentService {
    /**
     * 添加评论
     *
     * @param vo  评论请求参数
     * @param uid 用户ID
     * @return 评论ID
     */
    Long addComment(CommentAddReqVO vo, Long uid);

    /**
     * 上传评论图片
     *
     * @param multipartFile 图片文件
     * @param uid           用户ID
     * @return 图片URL
     */
    String uploadImage(MultipartFile multipartFile, String uid);

    /**
     * 删除评论
     *
     * @param commentId 评论ID
     * @param uid       用户ID
     */
    void deleteComment(Long commentId, Long uid);

    /**
     * 查询帖子的评论列表
     *
     * @param postId     帖子ID
     * @param currentUid 当前用户ID
     * @return 评论列表
     */
    List<CommentRespVO> getComments(Long postId, Long currentUid);
}
