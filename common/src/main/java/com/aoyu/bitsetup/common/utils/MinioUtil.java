package com.aoyu.bitsetup.common.utils;

import com.aoyu.bitsetup.common.properties.MinioProperties;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MinioUtil {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioProperties minioProperties;

    /**
     * 检查存储桶是否存在
     */
    public boolean bucketExists(String bucketName) {
        try {
            return minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );
        } catch (Exception e) {
            log.error("检查存储桶是否存在失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 创建存储桶
     */
    public void createBucket(String bucketName) {
        try {
            if (!bucketExists(bucketName)) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucketName).build()
                );
                log.info("创建存储桶成功: {}", bucketName);
            }
        } catch (Exception e) {
            log.error("创建存储桶失败: {}", e.getMessage());
            throw new RuntimeException("创建存储桶失败", e);
        }
    }

    /**
     * 创建业务文件夹（实际是创建一个空对象作为文件夹标识）
     * MinIO没有真正的文件夹概念，但可以创建一个以 / 结尾的空对象来模拟
     *
     * @param bucketName 存储桶名称
     * @param folderPath 文件夹路径，如: "user/avatar/"
     */
    public void createFolder(String bucketName, String folderPath) {
        try {
            // 确保路径以 / 结尾
            if (!folderPath.endsWith("/")) {
                folderPath = folderPath + "/";
            }

            // 创建空对象作为文件夹
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(folderPath)
                            .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                            .build()
            );
            log.info("创建文件夹成功: {}", folderPath);
        } catch (Exception e) {
            log.error("创建文件夹失败: {}", e.getMessage());
            throw new RuntimeException("创建文件夹失败", e);
        }
    }

    /**
     * 检查文件夹是否存在
     *
     * @param bucketName 存储桶名称
     * @param folderPath 文件夹路径
     * @return 是否存在
     */
    public boolean folderExists(String bucketName, String folderPath) {
        try {
            if (!folderPath.endsWith("/")) {
                folderPath = folderPath + "/";
            }

            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(folderPath)
                            .maxKeys(1)
                            .build()
            );

            return results.iterator().hasNext();
        } catch (Exception e) {
            log.error("检查文件夹是否存在失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 上传文件到业务文件夹（推荐使用）
     *
     * @param file         文件
     * @param businessType 业务类型，如: "user/avatar", "product/image", "order/document"
     * @return 文件完整路径
     */
    public String uploadFileToFolder(MultipartFile file, String businessType) {
        return uploadFileToFolder(minioProperties.getBucketName(), file, businessType);
    }

    /**
     * 上传文件到指定存储桶的业务文件夹
     *
     * @param bucketName   存储桶名称
     * @param file         文件
     * @param businessType 业务类型
     * @return 文件完整路径
     */
    public String uploadFileToFolder(String bucketName, MultipartFile file, String businessType) {
        try {
            // 确保存储桶存在
            createBucket(bucketName);

            // 构建文件夹路径：业务类型/日期/
            String folderPath = buildFolderPath(businessType);

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString().replace("-", "") + extension;

            // 完整的对象名称：文件夹路径 + 文件名
            String objectName = folderPath + fileName;

            // 上传文件
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            log.info("文件上传成功: {}", objectName);
            return objectName;
        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage());
            throw new RuntimeException("文件上传失败", e);
        }
    }

    /**
     * 上传文件到业务文件夹（保留原文件名）
     *
     * @param file             文件
     * @param businessType     业务类型
     * @param keepOriginalName 是否保留原文件名
     * @return 文件完整路径
     */
    public String uploadFileToFolder(MultipartFile file, String businessType, boolean keepOriginalName) {
        try {
            String bucketName = minioProperties.getBucketName();
            createBucket(bucketName);

            String folderPath = buildFolderPath(businessType);
            String originalFilename = file.getOriginalFilename();

            String fileName;
            if (keepOriginalName) {
                // 保留原文件名，但添加时间戳避免重复
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String baseName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
                fileName = baseName + "_" + System.currentTimeMillis() + extension;
            } else {
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                fileName = UUID.randomUUID().toString().replace("-", "") + extension;
            }

            String objectName = folderPath + fileName;

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            log.info("文件上传成功: {}", objectName);
            return "http://localhost:9000/" + bucketName + "/" + objectName;
        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage());
            throw new RuntimeException("文件上传失败", e);
        }
    }

    /**
     * 构建文件夹路径：业务类型/年月日/
     * 例如: user/avatar/20250101/
     */
    private String buildFolderPath(String businessType) {
        // 移除首尾的斜杠
        businessType = businessType.replaceAll("^/+|/+$", "");

        // 构建路径
        return businessType + "/";
    }

    /**
     * 构建自定义文件夹路径（不带日期）
     */
    private String buildSimpleFolderPath(String businessType) {
        businessType = businessType.replaceAll("^/+|/+$", "");
        return businessType + "/";
    }

    /**
     * 列出业务文件夹中的所有文件
     *
     * @param bucketName   存储桶名称
     * @param businessType 业务类型
     * @return 文件名列表
     */
    public List<String> listFilesByFolder(String bucketName, String businessType) {
        try {
            List<String> fileNames = new ArrayList<>();
            String prefix = buildSimpleFolderPath(businessType);

            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(prefix)
                            .recursive(true) // 递归列出所有子目录文件
                            .build()
            );

            for (Result<Item> result : results) {
                Item item = result.get();
                // 过滤掉文件夹本身（以/结尾的空对象）
                if (!item.objectName().endsWith("/")) {
                    fileNames.add(item.objectName());
                }
            }
            return fileNames;
        } catch (Exception e) {
            log.error("列出文件失败: {}", e.getMessage());
            throw new RuntimeException("列出文件失败", e);
        }
    }

    /**
     * 删除业务文件夹及其所有文件
     *
     * @param bucketName   存储桶名称
     * @param businessType 业务类型
     */
    public void deleteFolder(String bucketName, String businessType) {
        try {
            String prefix = buildSimpleFolderPath(businessType);
            List<String> files = listFilesByFolder(bucketName, businessType);

            if (!files.isEmpty()) {
                List<DeleteObject> objects = new ArrayList<>();
                for (String file : files) {
                    objects.add(new DeleteObject(file));
                }

                // 批量删除文件
                Iterable<Result<DeleteError>> results = minioClient.removeObjects(
                        RemoveObjectsArgs.builder()
                                .bucket(bucketName)
                                .objects(objects)
                                .build()
                );

                for (Result<DeleteError> result : results) {
                    DeleteError error = result.get();
                    log.error("删除文件失败: {}", error.message());
                }
            }

            log.info("删除文件夹成功: {}", prefix);
        } catch (Exception e) {
            log.error("删除文件夹失败: {}", e.getMessage());
            throw new RuntimeException("删除文件夹失败", e);
        }
    }

    // ============ 以下是原有方法保持不变 ============

    /**
     * 获取所有存储桶
     */
    public List<String> getAllBuckets() {
        try {
            List<Bucket> buckets = minioClient.listBuckets();
            List<String> bucketNames = new ArrayList<>();
            for (Bucket bucket : buckets) {
                bucketNames.add(bucket.name());
            }
            return bucketNames;
        } catch (Exception e) {
            log.error("获取存储桶列表失败: {}", e.getMessage());
            throw new RuntimeException("获取存储桶列表失败", e);
        }
    }

    /**
     * 删除存储桶
     */
    public void removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(
                    RemoveBucketArgs.builder().bucket(bucketName).build()
            );
            log.info("删除存储桶成功: {}", bucketName);
        } catch (Exception e) {
            log.error("删除存储桶失败: {}", e.getMessage());
            throw new RuntimeException("删除存储桶失败", e);
        }
    }

    /**
     * 上传文件（原方法，直接上传到桶根目录）
     */
    public String uploadFile(MultipartFile file) {
        return uploadFile(minioProperties.getBucketName(), file);
    }

    /**
     * 上传文件到指定存储桶（原方法）
     */
    public String uploadFile(String bucketName, MultipartFile file) {
        try {
            createBucket(bucketName);
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString().replace("-", "") + extension;

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            log.info("文件上传成功: {}", fileName);
            return fileName;
        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage());
            throw new RuntimeException("文件上传失败", e);
        }
    }

    /**
     * 上传文件流
     */
    public String uploadFile(String bucketName, String objectName, InputStream inputStream, String contentType) {
        try {
            createBucket(bucketName);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(contentType)
                            .build()
            );
            log.info("文件流上传成功: {}", objectName);
            return objectName;
        } catch (Exception e) {
            log.error("文件流上传失败: {}", e.getMessage());
            throw new RuntimeException("文件流上传失败", e);
        }
    }

    /**
     * 下载文件
     */
    public InputStream downloadFile(String fileName) {
        return downloadFile(minioProperties.getBucketName(), fileName);
    }

    /**
     * 从指定存储桶下载文件
     */
    public InputStream downloadFile(String bucketName, String fileName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            log.error("文件下载失败: {}", e.getMessage());
            throw new RuntimeException("文件下载失败", e);
        }
    }

    /**
     * 删除文件
     */
    public void deleteFile(String fileName) {
        deleteFile(minioProperties.getBucketName(), fileName);
    }

    /**
     * 从指定存储桶删除文件
     */
    public void deleteFile(String bucketName, String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
            log.info("文件删除成功: {}", fileName);
        } catch (Exception e) {
            log.error("文件删除失败: {}", e.getMessage());
            throw new RuntimeException("文件删除失败", e);
        }
    }

    /**
     * 批量删除文件
     */
    public void deleteFiles(String bucketName, List<String> fileNames) {
        try {
            List<DeleteObject> objects = new ArrayList<>();
            for (String fileName : fileNames) {
                objects.add(new DeleteObject(fileName));
            }

            Iterable<Result<DeleteError>> results = minioClient.removeObjects(
                    RemoveObjectsArgs.builder()
                            .bucket(bucketName)
                            .objects(objects)
                            .build()
            );

            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                log.error("批量删除文件失败: {}", error.message());
            }
            log.info("批量删除文件成功");
        } catch (Exception e) {
            log.error("批量删除文件失败: {}", e.getMessage());
            throw new RuntimeException("批量删除文件失败", e);
        }
    }

    /**
     * 获取文件访问URL（临时访问链接）
     */
    public String getFileUrl(String fileName) {
        return getFileUrl(minioProperties.getBucketName(), fileName, 7, TimeUnit.DAYS);
    }

    /**
     * 获取文件访问URL（自定义过期时间）
     */
    public String getFileUrl(String bucketName, String fileName, int duration, TimeUnit unit) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(fileName)
                            .expiry(duration, unit)
                            .build()
            );
        } catch (Exception e) {
            log.error("获取文件URL失败: {}", e.getMessage());
            throw new RuntimeException("获取文件URL失败", e);
        }
    }

    /**
     * 列出存储桶中的所有文件
     */
    public List<String> listFiles(String bucketName) {
        try {
            List<String> fileNames = new ArrayList<>();
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(bucketName).build()
            );

            for (Result<Item> result : results) {
                Item item = result.get();
                fileNames.add(item.objectName());
            }
            return fileNames;
        } catch (Exception e) {
            log.error("列出文件失败: {}", e.getMessage());
            throw new RuntimeException("列出文件失败", e);
        }
    }

    /**
     * 检查文件是否存在
     */
    public boolean fileExists(String bucketName, String fileName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 复制文件
     */
    public void copyFile(String sourceBucket, String sourceObject,
                         String targetBucket, String targetObject) {
        try {
            minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .source(CopySource.builder()
                                    .bucket(sourceBucket)
                                    .object(sourceObject)
                                    .build())
                            .bucket(targetBucket)
                            .object(targetObject)
                            .build()
            );
            log.info("文件复制成功: {} -> {}", sourceObject, targetObject);
        } catch (Exception e) {
            log.error("文件复制失败: {}", e.getMessage());
            throw new RuntimeException("文件复制失败", e);
        }
    }
}