package com.aoyu.bitsetup.model.vo.community;

import com.aoyu.bitsetup.model.dto.community.PostDetailDTO;
import com.aoyu.bitsetup.model.dto.user.UserDetailInfoDTO;
import lombok.Data;

/**
 * @ClassName：PostDetailRespVO
 * @Author: aoyu
 * @Date: 2025-10-02 14:46
 * @Description: 帖子详情响应VO
 */

@Data
public class PostDetailRespVO {

    private PostDetailDTO post;
    private UserDetailInfoDTO user;

}
