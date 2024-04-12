package online.zust.qcqcqc.services.module.attachment.handler;

import online.zust.qcqcqc.services.module.attachment.entity.dto.FileUpload;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author qcqcqc
 * Date: 2024/3/18
 * Time: 23:04
 */
public interface AttachmentsStorageHandler {
    /**
     * 上传附件
     * @param attachment 附件
     * @return 实际文件名
     */
    String upload(MultipartFile attachment);

    /**
     * 获取附件外部访问地址
     * @param fileName 文件名
     * @return 外部访问地址
     */
    String getPublicUrl(String fileName);

    /**
     * 删除附件
     * @param fileName 文件名
     * @return 是否删除成功
     */
    boolean deleteByFileName(String fileName);

    /**
     * 获取文件输入流
     * @param fileName 文件名
     * @return 文件输入流
     */
    InputStream getFileInputstream(String fileName);

    /**
     * 上传文件
     * @param file 文件
     * @return 实际文件名
     */
    String upload(FileUpload file);
}
