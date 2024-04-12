package online.zust.qcqcqc.services.module.attachment.handler.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.zust.qcqcqc.services.module.attachment.config.MinioConfig;
import online.zust.qcqcqc.services.module.attachment.entity.dto.FileUpload;
import online.zust.qcqcqc.services.module.attachment.exception.AttachmentServiceException;
import online.zust.qcqcqc.services.module.attachment.handler.AttachmentsStorageHandler;
import online.zust.qcqcqc.services.module.attachment.utils.DateUtil;
import online.zust.qcqcqc.services.module.attachment.utils.FileUtils;
import online.zust.qcqcqc.services.module.attachment.utils.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qcqcqc
 * Date: 2024/3/18
 * Time: 23:17
 */
@Component
@ConditionalOnProperty(name = "storage.type", havingValue = "minio")
@RequiredArgsConstructor
@Slf4j
public class MinioStorageHandler implements AttachmentsStorageHandler {

    private final MinioConfig prop;
    private final MinioClient minioClient;
    @Value("${storage.minio.public-url}")
    private String publicUrl;

    @Override
    public String upload(MultipartFile attachment) {
        String originalFilename = attachment.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            throw new RuntimeException();
        }
        String fileName = genStoreName(originalFilename);
        String objectName = DateUtil.format(DateUtil.date(), "yyyy-MM/dd") + "/" + fileName;
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(prop.getBucketName()).object(objectName)
                    .stream(attachment.getInputStream(), attachment.getSize(), -1).contentType(attachment.getContentType()).build();
            //文件名称相同会覆盖
            minioClient.putObject(objectArgs);
        } catch (Exception e) {
            log.error("文件上传失败: " + e.getMessage());
            throw new AttachmentServiceException("文件上传失败");
        }
        return objectName;
    }

    private String genStoreName(String originalFilename) {
        return FileUtils.getNameWithoutSuffix(originalFilename) + "-" + UUID.fastUUID() + "." + FileUtils.getSuffix(originalFilename);
    }

    @Override
    public boolean deleteByFileName(String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(prop.getBucketName()).object(fileName).build());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public InputStream getFileInputstream(String fileName) {
        try {
            GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(prop.getBucketName()).object(fileName).build();
            return minioClient.getObject(objectArgs);
        } catch (Exception e) {
            log.error("获取文件输入流失败: " + e.getMessage());
            throw new AttachmentServiceException("获取文件输入流失败");
        }
    }

    @Override
    public String upload(FileUpload file) {
        String base64 = file.getBase64();
        try {
            Path tempFile = Files.createTempFile(UUID.fastUUID().toString(), file.getFileName());
            File file1 = FileUtils.base64ToFile(base64, tempFile);
            // 将文件上传到minio
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(prop.getBucketName()).object(file1.getName())
                    .stream(Files.newInputStream(tempFile), Files.size(tempFile), -1).contentType(file.getFileType()).build();
            minioClient.putObject(objectArgs);
            return file1.getName();
        } catch (Exception e) {
            log.error("创建临时文件失败: " + e.getMessage());
            throw new AttachmentServiceException("创建临时文件失败");
        }
    }

    @Override
    public String getPublicUrl(String fileName) {
        // 考虑到配置文件内不一定以/结尾，所以这里做一下处理
        return publicUrl.endsWith("/") ? publicUrl + fileName : publicUrl + "/" + fileName;
    }

    /**
     * 查看存储bucket是否存在
     *
     * @return boolean
     */
    public Boolean bucketExists(String bucketName) {
        boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            log.error("查看存储bucket是否存在失败: " + e.getMessage());
            return false;
        }
        return found;
    }

    /**
     * 创建存储bucket
     *
     * @return Boolean
     */
    public Boolean makeBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            log.error("创建存储bucket失败: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 删除存储bucket
     *
     * @return Boolean
     */
    public Boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            log.error("删除存储bucket失败: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 获取全部bucket
     */
    public List<Bucket> getAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            log.error("获取全部bucket失败: " + e.getMessage());
            throw new AttachmentServiceException("获取全部bucket失败");
        }
    }

    /**
     * 预览图片
     *
     * @param fileName 文件名称
     * @return String
     */
    public String preview(String fileName) {
        // 查看文件地址
        GetPresignedObjectUrlArgs build = GetPresignedObjectUrlArgs.builder().bucket(prop.getBucketName()).object(fileName).method(Method.GET).build();
        try {
            return minioClient.getPresignedObjectUrl(build);
        } catch (Exception e) {
            log.error("获取预览图片失败: " + e.getMessage());
            throw new AttachmentServiceException("获取预览图片失败");
        }
    }

    /**
     * 文件下载
     *
     * @param fileName 文件名称
     * @param res      response
     * @return Boolean
     */
    public void download(String fileName, HttpServletResponse res) {
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(prop.getBucketName())
                .object(fileName).build();
        try (GetObjectResponse response = minioClient.getObject(objectArgs)) {
            byte[] buf = new byte[1024];
            int len;
            try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
                while ((len = response.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                os.flush();
                byte[] bytes = os.toByteArray();
                res.setCharacterEncoding("utf-8");
                // 设置强制下载不打开
                res.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
                try (ServletOutputStream stream = res.getOutputStream()) {
                    stream.write(bytes);
                    stream.flush();
                }
            }
        } catch (Exception e) {
            log.error("文件下载失败: " + e.getMessage());
            throw new AttachmentServiceException("文件下载失败");
        }
    }

    /**
     * 查看文件对象
     *
     * @return 存储bucket内文件对象信息
     */
    public List<Item> listObjects() {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(prop.getBucketName()).build());
        List<Item> items = new ArrayList<>();
        try {
            for (Result<Item> result : results) {
                items.add(result.get());
            }
        } catch (Exception e) {
            log.error("获取文件对象失败: " + e.getMessage());
            throw new AttachmentServiceException("获取文件对象失败");
        }
        return items;
    }

}
