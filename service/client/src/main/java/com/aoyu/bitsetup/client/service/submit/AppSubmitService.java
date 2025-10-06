package com.aoyu.bitsetup.client.service.submit;

import com.aoyu.bitsetup.model.dto.submit.SubmitDraftDTO;
import com.aoyu.bitsetup.model.vo.submit.AppDevelopSubmitReqVO;
import com.aoyu.bitsetup.model.vo.submit.AppRecommendSubmitReqVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @InterfaceName：AppSubmitService
 * @Author: aoyu
 * @Date: 2025/10/5 下午1:44
 * @Description:
 */

public interface AppSubmitService {
    String uploadImg(MultipartFile file, String uid);

    void submitRecommend(AppRecommendSubmitReqVO recommendReqVO);

    void submitDevelop(AppDevelopSubmitReqVO appDevelopSubmitReqVO);

    SubmitDraftDTO getSubmitDraft(String type,String uid);

    String uploadFile(MultipartFile file, String uid);
}
