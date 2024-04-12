package online.zust.qcqcqc.services.module.attachment.service;

import online.zust.qcqcqc.services.module.attachment.entity.Attachment;
import online.zust.qcqcqc.services.module.attachment.entity.dto.FileUpload;
import online.zust.qcqcqc.utils.IServiceEnhance;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author pqcmm
 */
public interface AttachmentService extends IServiceEnhance<Attachment> {
    /**
     * 保存文件到附件
     *
     * @param file 文件
     * @return 附件
     */
    Attachment saveFileToAttachment(FileUpload file);

    /**
     * 保存文件到附件
     *
     * @param file 文件
     * @return 附件
     * @throws IOException IO异常
     */
    Attachment saveFileToAttachment(MultipartFile file) throws IOException;

    /**
     * 删除附件
     *
     * @param fileName 文件名
     * @return 是否删除成功
     */
    boolean deleteByFileName(String fileName);

    /**
     * 删除附件
     *
     * @param publicUrl 外部访问地址
     * @return 是否删除成功
     */
    boolean deleteByPublicUrl(String publicUrl);

    /**
     * 获取文件输入流
     *
     * @param fileName 文件名
     * @return 文件输入流
     */
    InputStream getFileInputstream(String fileName);
}
