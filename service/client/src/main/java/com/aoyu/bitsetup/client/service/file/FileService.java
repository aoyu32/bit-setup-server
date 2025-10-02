package com.aoyu.bitsetup.client.service.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @InterfaceName：FileService
 * @Author: aoyu
 * @Date: 2025/10/1 下午7:54
 * @Description:
 */

public interface FileService {

    String fileUpload(MultipartFile file,String resourceId,String businessType);

}
