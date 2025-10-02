package com.aoyu.bitsetup.client.service.community;

import com.aoyu.bitsetup.model.dto.community.PostDetailDTO;
import com.aoyu.bitsetup.model.dto.community.PostHotDTO;
import com.aoyu.bitsetup.model.dto.community.PostInfoDTO;
import com.aoyu.bitsetup.model.dto.community.PostRecommendDTO;
import com.aoyu.bitsetup.model.entity.community.Post;
import com.aoyu.bitsetup.model.vo.community.PostDetailRespVO;
import com.aoyu.bitsetup.model.vo.community.PostPutReqVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @InterfaceName：PostService
 * @Author: aoyu
 * @Date: 2025/10/1 下午2:19
 * @Description:
 */

public interface PostService {

    void savePost(PostPutReqVO postPutReqVO);

    String uploadPostImage(MultipartFile multipartFile,String uid);

    List<PostInfoDTO> getPostList(String category);

    PostDetailRespVO getPostDetail(String postId);

    List<PostHotDTO> getHotPostList();

    List<PostRecommendDTO> getRecommendPostList();

}
