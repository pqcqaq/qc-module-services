package online.zust.qcqcqc.services.module.attachment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.zust.qcqcqc.services.module.attachment.entity.Attachment;
import online.zust.qcqcqc.services.module.attachment.entity.dto.FileUpload;
import online.zust.qcqcqc.services.module.attachment.handler.AttachmentsStorageHandler;
import online.zust.qcqcqc.services.module.attachment.mapper.AttachmentMapper;
import online.zust.qcqcqc.services.module.attachment.service.AttachmentService;
import online.zust.qcqcqc.services.module.attachment.utils.FileUtils;
import online.zust.qcqcqc.utils.EnhanceService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author pqcmm
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentsServiceImpl extends EnhanceService<AttachmentMapper, Attachment> implements AttachmentService {
    private final AttachmentsStorageHandler attachmentsStorageHandler;

    @NotNull
    @Override
    public Attachment saveFileToAttachment(FileUpload file) {
        String md5 = FileUtils.calMd5(file.getBase64());
        // 检查md5是否存在
        Attachment one = this.lambdaQuery().eq(Attachment::getMd5, md5).one();
        if (one != null) {
            return one;
        }
        String filename = attachmentsStorageHandler.upload(file);
        String publicUrl = attachmentsStorageHandler.getPublicUrl(filename);
        Attachment attachment = new Attachment();
        attachment.setOriginalFilename(filename);
        attachment.setUrl(publicUrl);
        attachment.setFileType(FileUtils.getFileType(file.getFileName()));
        attachment.setMd5(md5);
        attachment.setTag(file.getTag());
        this.save(attachment);
        return attachment;
    }

    @NotNull
    @Override
    public Attachment saveFileToAttachment(MultipartFile file) throws IOException {
        String md5 = FileUtils.calMd5(file.getInputStream());
        // 检查md5是否存在
        Attachment one = this.lambdaQuery().eq(Attachment::getMd5, md5).one();
        if (one != null) {
            return one;
        }
        String filename = attachmentsStorageHandler.upload(file);
        String publicUrl = attachmentsStorageHandler.getPublicUrl(filename);
        Attachment attachment = new Attachment();
        attachment.setOriginalFilename(filename);
        attachment.setUrl(publicUrl);
        attachment.setFileType(FileUtils.getFileType(Objects.requireNonNull(file.getOriginalFilename())));
        attachment.setMd5(md5);
        attachment.setTag("default");
        this.save(attachment);
        return attachment;
    }

    @Override
    public boolean deleteByFileName(String fileName) {
        return attachmentsStorageHandler.deleteByFileName(fileName);
    }

    @Override
    public boolean deleteByPublicUrl(String publicUrl) {
        // 获取最后的文件名
        String url = publicUrl.endsWith("/") ? publicUrl.substring(0, publicUrl.length() - 1) : publicUrl;
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        return attachmentsStorageHandler.deleteByFileName(fileName);
    }

    @Override
    public InputStream getFileInputstream(String fileName) {
        return attachmentsStorageHandler.getFileInputstream(fileName);
    }
}
