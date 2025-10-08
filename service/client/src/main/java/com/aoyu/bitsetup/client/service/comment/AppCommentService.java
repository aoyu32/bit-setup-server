package com.aoyu.bitsetup.client.service.comment;

import com.aoyu.bitsetup.model.vo.comment.CommentAddReqVO;
import com.aoyu.bitsetup.model.vo.comment.CommentRespVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @InterfaceName：AppCommentService
 * @Author: aoyu
 * @Date: 2025/10/7 上午12:29
 * @Description:
 */

public interface AppCommentService {
    /**
     * 发表评论
     */
    Long addComment(CommentAddReqVO vo, Long uid);

    /**
     * 修改评论
     */
//    void updateComment(CommentUpdateVO vo, Long uid);

    void saveCommentImages(Long commentId, List<String> imageUrls);


    String uploadImage(MultipartFile file,String uid);

    /**
     * 删除评论
     */
    void deleteComment(Long commentId, Long uid);

    /**
     * 获取评论列表
     */
//    Page<CommentDTO> getCommentList(CommentListVO vo, Long currentUid);
    List<CommentRespVO> getComments(Long aid, Long currentUid);

    /**
     * 获取评论详情
     */
//    CommentDTO getCommentDetail(Long commentId, Long currentUid);
}
