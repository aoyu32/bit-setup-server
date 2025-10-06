package com.aoyu.bitsetup.client.service.submit.impl;

import com.aoyu.bitsetup.client.mapper.submit.AppSubmitFileMapper;
import com.aoyu.bitsetup.client.mapper.submit.AppSubmitMapper;
import com.aoyu.bitsetup.client.service.submit.AppSubmitService;
import com.aoyu.bitsetup.common.enumeration.ResultCode;
import com.aoyu.bitsetup.common.exception.BusinessException;
import com.aoyu.bitsetup.common.utils.MinioUtil;
import com.aoyu.bitsetup.common.utils.ThreadLocalUtil;
import com.aoyu.bitsetup.model.dto.submit.SubmitDraftDTO;
import com.aoyu.bitsetup.model.entity.submit.AppSubmit;
import com.aoyu.bitsetup.model.entity.submit.AppSubmitFile;
import com.aoyu.bitsetup.model.entity.submit.AppSubmitScreenshot;
import com.aoyu.bitsetup.model.vo.submit.AppDevelopSubmitReqVO;
import com.aoyu.bitsetup.model.vo.submit.AppRecommendSubmitReqVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @ClassName：AppSubmitServiceImpl
 * @Author: aoyu
 * @Date: 2025-10-05 13:44
 * @Description: 应用提交接口实现
 */

@Service
@Slf4j
public class AppSubmitServiceImpl implements AppSubmitService {

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    private AppSubmitMapper appSubmitMapper;

    @Autowired
    private AppSubmitFileMapper appSubmitFileMapper;

    @Override
    public String uploadImg(MultipartFile file, String uid) {
        String path = "submit/screenshot/" + uid;
        return minioUtil.uploadFileToFolder(file, path, true);
    }

    @Override
    @Transactional
    public void submitRecommend(AppRecommendSubmitReqVO recommendReqVO) {


        Long uid = (Long)ThreadLocalUtil.get("uid");
        AppSubmit appSubmit = new AppSubmit();
        appSubmit.setUid(uid);
        appSubmit.setAppName(recommendReqVO.getAppName());
        appSubmit.setBrief(recommendReqVO.getBio());
        appSubmit.setCategoryId(recommendReqVO.getSecondaryCategory());
        appSubmit.setDownloadUrl(recommendReqVO.getDownloadUrl());
        if (recommendReqVO.getSize() != null) {
            appSubmit.setFileSize((long) (recommendReqVO.getSize() * 1024));
        } else {
            appSubmit.setFileSize(null);
        }
        appSubmit.setIsDraft(recommendReqVO.getIsDraft());
        appSubmit.setOfficialWebsite(recommendReqVO.getOfficialUrl());
        appSubmit.setVersion(recommendReqVO.getVersion());
        if (!recommendReqVO.getFeeType().isEmpty()) {
            appSubmit.setPricingModel(recommendReqVO.getFeeType());
        }
        appSubmit.setPlatformId(4);

        //查询草稿
        LambdaQueryWrapper<AppSubmit> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AppSubmit::getUid,uid)
                .eq(AppSubmit::getIsDraft,1);
        AppSubmit appSubmit1 = appSubmitMapper.selectOne(lambdaQueryWrapper);
        log.info("查询到的草稿信息：{}",appSubmit1);
        if(appSubmit1==null){
            int insert = appSubmitMapper.insert(appSubmit);
            if (insert == 0) {
                throw new BusinessException(ResultCode.SUBMIT_ERROR);
            }
        }else {
            appSubmit.setId(appSubmit1.getId());
            int update = appSubmitMapper.updateById(appSubmit);
            if (update == 0) {
                throw new BusinessException(ResultCode.SUBMIT_ERROR);
            }
        }

          // 插入截图
        List<String> screenshots = Optional.ofNullable(recommendReqVO.getScreenshots())
                .orElseGet(ArrayList::new);

        List<String> validImages = screenshots.stream()
                .filter(Objects::nonNull)
                .filter(s -> !s.trim().isEmpty())
                .toList();

        if (!validImages.isEmpty()) {
            List<AppSubmitScreenshot> collect = IntStream.range(0, validImages.size())
                    .mapToObj(i -> {
                        String screenshot = validImages.get(i);

                        AppSubmitScreenshot appSubmitScreenshot = new AppSubmitScreenshot();
                        appSubmitScreenshot.setId(IdWorker.getId());
                        appSubmitScreenshot.setSubmissionId(appSubmit.getId()); // 投稿ID
                        appSubmitScreenshot.setImageUrl(screenshot);

                        // 提取文件后缀（格式安全判断）
                        String imageFormat = "unknown";
                        int dotIndex = screenshot.lastIndexOf('.');
                        if (dotIndex > 0 && dotIndex < screenshot.length() - 1) {
                            imageFormat = screenshot.substring(dotIndex + 1);
                        }
                        appSubmitScreenshot.setImageFormat(imageFormat);

                        // 使用 i + 1 作为顺序
                        appSubmitScreenshot.setSortOrder(i + 1);

                        return appSubmitScreenshot;
                    })
                    .toList();


            if (appSubmit1 == null){
                int inserted = appSubmitMapper.insertBatch(collect);
                if (inserted == 0) {
                    throw new BusinessException(ResultCode.SUBMIT_ERROR);
                }
            }else {
                // 1. 删除该投稿的所有旧截图
                LambdaQueryWrapper<AppSubmitScreenshot> screenshotWrapper = new LambdaQueryWrapper<>();
                screenshotWrapper.eq(AppSubmitScreenshot::getSubmissionId, appSubmit.getId());
                appSubmitMapper.deleteScreenshots(appSubmit1.getId()); // 需要添加这个方法
                // 2. 插入新截图
                int inserted = appSubmitMapper.insertBatch(collect);
                if (inserted == 0) {
                    throw new BusinessException(ResultCode.SUBMIT_ERROR);
                }
            }
        }
    }

    @Override
    @Transactional
    public void submitDevelop(AppDevelopSubmitReqVO developSubmitReqVO) {
        Long uid = (Long)ThreadLocalUtil.get("uid");
        AppSubmit appSubmit = new AppSubmit();
        appSubmit.setUid(uid);
        appSubmit.setAppName(developSubmitReqVO.getAppName());
        appSubmit.setBrief(developSubmitReqVO.getBio());
        appSubmit.setCategoryId(developSubmitReqVO.getSecondaryCategory());
        appSubmit.setDownloadUrl(developSubmitReqVO.getDownloadUrl());
        if (developSubmitReqVO.getSize() != null) {
            appSubmit.setFileSize((long) (developSubmitReqVO.getSize() * 1024));
        } else {
            appSubmit.setFileSize(null);
        }
        appSubmit.setIsDraft(developSubmitReqVO.getIsDraft());
        appSubmit.setOfficialWebsite(developSubmitReqVO.getOfficialUrl());
        appSubmit.setVersion(developSubmitReqVO.getVersion());
        if (!developSubmitReqVO.getFeeType().isEmpty()) {
            appSubmit.setPricingModel(developSubmitReqVO.getFeeType());
        }
        appSubmit.setPlatformId(4);
        appSubmit.setIsPersonalDevelop(true);
        appSubmit.setIconUrl(developSubmitReqVO.getIconUrl());

        // ✅ 查询草稿
        LambdaQueryWrapper<AppSubmit> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AppSubmit::getUid, uid)
                .eq(AppSubmit::getIsDraft, 1)
                .eq(AppSubmit::getIsPersonalDevelop, 1); // ✅ 增加开发者标识
        AppSubmit appSubmit1 = appSubmitMapper.selectOne(lambdaQueryWrapper);
        log.info("查询到的开发者草稿信息：{}", appSubmit1);

        if (appSubmit1 == null) {
            // 新增草稿
            int insert = appSubmitMapper.insert(appSubmit);
            if (insert == 0) {
                throw new BusinessException(ResultCode.SUBMIT_ERROR);
            }
        } else {
            // 更新草稿
            appSubmit.setId(appSubmit1.getId());
            int update = appSubmitMapper.updateById(appSubmit);
            if (update == 0) {
                throw new BusinessException(ResultCode.SUBMIT_ERROR);
            }
        }

        // 插入截图
        List<String> screenshots = Optional.ofNullable(developSubmitReqVO.getScreenshots())
                .orElseGet(ArrayList::new);
        List<String> proveImages = Optional.ofNullable(developSubmitReqVO.getProveImages())
                .orElseGet(ArrayList::new);

        // 合并两类截图
        List<String> allImages = new ArrayList<>();
        allImages.addAll(screenshots);
        allImages.addAll(proveImages);

        // 过滤空值
        List<String> validImages = allImages.stream()
                .filter(Objects::nonNull)
                .filter(s -> !s.trim().isEmpty())
                .toList();

        // 批量插入截图
        if (!validImages.isEmpty()) {
            List<AppSubmitScreenshot> collect = IntStream.range(0, validImages.size())
                    .mapToObj(i -> {
                        String screenshot = validImages.get(i);
                        AppSubmitScreenshot appSubmitScreenshot = new AppSubmitScreenshot();
                        appSubmitScreenshot.setId(IdWorker.getId());
                        appSubmitScreenshot.setSubmissionId(appSubmit.getId());
                        appSubmitScreenshot.setImageUrl(screenshot);

                        // 安全提取图片后缀
                        String imageFormat = "unknown";
                        int dotIndex = screenshot.lastIndexOf('.');
                        if (dotIndex > 0 && dotIndex < screenshot.length() - 1) {
                            imageFormat = screenshot.substring(dotIndex + 1);
                        }
                        appSubmitScreenshot.setImageFormat(imageFormat);
                        appSubmitScreenshot.setSortOrder(i + 1);
                        return appSubmitScreenshot;
                    })
                    .toList();

            if (appSubmit1 == null) {
                // 新增草稿，直接插入截图
                int inserted = appSubmitMapper.insertBatch(collect);
                if (inserted == 0) {
                    throw new BusinessException(ResultCode.SUBMIT_ERROR);
                }
            } else {
                // ✅ 更新草稿，先删除旧截图再插入新截图
                appSubmitMapper.deleteScreenshots(appSubmit.getId());
                int inserted = appSubmitMapper.insertBatch(collect);
                if (inserted == 0) {
                    throw new BusinessException(ResultCode.SUBMIT_ERROR);
                }
            }
        }

        // 插入上传的应用文件
        String fileUrl = developSubmitReqVO.getFileUrl();
        if (fileUrl != null && !fileUrl.trim().isEmpty()) {
            AppSubmitFile appSubmitFile = new AppSubmitFile();
            int lastSlashIndex = fileUrl.lastIndexOf('/');
            String fileName = (lastSlashIndex != -1 && lastSlashIndex < fileUrl.length() - 1)
                    ? fileUrl.substring(lastSlashIndex + 1)
                    : "unknown_file";

            appSubmitFile.setId(IdWorker.getId());
            appSubmitFile.setSubmissionId(appSubmit.getId());
            appSubmitFile.setFileName(fileName);
            appSubmitFile.setFileUrl(fileUrl);
            appSubmitFile.setFileSize(developSubmitReqVO.getSize() != null
                    ? Long.valueOf(developSubmitReqVO.getSize())
                    : null);

            if (appSubmit1 == null) {
                // 新增草稿，直接插入文件
                int insertedFile = appSubmitFileMapper.insert(appSubmitFile);
                if (insertedFile == 0) {
                    throw new BusinessException(ResultCode.SUBMIT_ERROR);
                }
            } else {
                // ✅ 更新草稿，先删除旧文件再插入新文件
                LambdaQueryWrapper<AppSubmitFile> fileWrapper = new LambdaQueryWrapper<>();
                fileWrapper.eq(AppSubmitFile::getSubmissionId, appSubmit.getId());
                appSubmitFileMapper.deleteFilesBySubmissionId(appSubmit1.getId());

                int insertedFile = appSubmitFileMapper.insert(appSubmitFile);
                if (insertedFile == 0) {
                    throw new BusinessException(ResultCode.SUBMIT_ERROR);
                }
            }
        }
    }

    @Override
    public SubmitDraftDTO getSubmitDraft(String type, String uid) {
        SubmitDraftDTO submitDraftDTO = null;

        if ("dev".equals(type)) {
            // 查询个人开发草稿
            submitDraftDTO = appSubmitMapper.selectDevelopSubmit(Long.valueOf(uid));
        } else if ("recommend".equals(type)) {
            // 查询推荐应用草稿
            submitDraftDTO = appSubmitMapper.selectRecommendSubmit(Long.valueOf(uid));
        }

        if (submitDraftDTO != null) {
            return submitDraftDTO;
        }

        return null;
//        throw new BusinessException(ResultCode.UNKNOWN_SUBMIT_TYPE);
    }

    @Override
    public String uploadFile(MultipartFile file, String uid) {
        String path = "submit/file/" + uid;
        return minioUtil.uploadFileToFolder(file, path, true);
    }

}
