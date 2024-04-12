package online.zust.qcqcqc.services.module.attachment.handler.impl;

import lombok.extern.slf4j.Slf4j;
import online.zust.qcqcqc.services.module.attachment.entity.dto.FileUpload;
import online.zust.qcqcqc.services.module.attachment.exception.AttachmentServiceException;
import online.zust.qcqcqc.services.module.attachment.handler.AttachmentsStorageHandler;
import online.zust.qcqcqc.services.module.attachment.utils.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author qcqcqc
 * Date: 2024/3/18
 * Time: 23:11
 */
@Component
@Slf4j
@ConditionalOnProperty(name = "storage.type", havingValue = "local")
public class LocalStorageHandler implements AttachmentsStorageHandler {
    static {
        log.info("正在使用本地存储，请确保本地存储路径正确，并手动配置外部链接地址");
    }

    @Value("${storage.local.path}")
    private String localPath;

    @Value("${storage.local.public}")
    private String publicUrl;

    @Override
    public String upload(MultipartFile attachment) {
        // 尝试压缩文件
        MultipartFile fileSave = FileUtils.compress(attachment);
        String storeFilename = getStoreFilename(fileSave);
        Path path = Paths.get(localPath, storeFilename);
        try {
            fileSave.transferTo(path);
        } catch (IOException e) {
            log.error("上传文件失败", e);
            throw new AttachmentServiceException("上传文件失败");
        }
        return storeFilename;
    }

    private String getStoreFilename(MultipartFile fileSave) {
        String originalFilename = fileSave.getOriginalFilename();
        assert originalFilename != null;
        return genFileName(originalFilename);
    }

    @Override
    public String getPublicUrl(String fileName) {
        // 考虑到配置文件内不一定以/结尾，所以这里做一下处理
        return publicUrl.endsWith("/") ? publicUrl + fileName : publicUrl + "/" + fileName;
    }

    @Override
    public boolean deleteByFileName(String fileName) {
        Path path = Paths.get(localPath, fileName);
        try {
            return path.toFile().delete();
        } catch (Exception e) {
            log.error("删除文件失败", e);
            throw new AttachmentServiceException("删除文件失败");
        }
    }

    @Override
    public InputStream getFileInputstream(String fileName) {
        Path path = Paths.get(localPath, fileName);
        try {
            return path.toUri().toURL().openStream();
        } catch (IOException e) {
            log.error("文件不存在", e);
            throw new AttachmentServiceException("文件不存在");
        }
    }

    @Override
    public String upload(FileUpload file) {
        String base64 = file.getBase64();
        String fileName = file.getFileName();
        String storeFilename = genFileName(fileName);
        Path path = Paths.get(localPath, storeFilename);
        try {
            FileUtils.base64ToFile(base64, path);
            return storeFilename;
        } catch (Exception e) {
            log.error("上传文件失败", e);
            throw new AttachmentServiceException("上传文件失败");
        }
    }

    private String genFileName(String originalFilename) {
        String nameWithoutSuffix = FileUtils.getNameWithoutSuffix(originalFilename);
        return nameWithoutSuffix + System.currentTimeMillis() + "." + FileUtils.getSuffix(originalFilename);
    }
}
