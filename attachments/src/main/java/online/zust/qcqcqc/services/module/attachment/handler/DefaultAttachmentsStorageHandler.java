package online.zust.qcqcqc.services.module.attachment.handler;

import online.zust.qcqcqc.services.exception.ServiceException;
import online.zust.qcqcqc.services.module.attachment.entity.dto.FileUpload;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author qcqcqc
 * Date: 2024/5/7
 * Time: 下午11:19
 */
public class DefaultAttachmentsStorageHandler implements AttachmentsStorageHandler {
    @Override
    public String upload(MultipartFile attachment) {
        throw new ServiceException("未配置文件存储方式");
    }

    @Override
    public String getPublicUrl(String fileName) {
        throw new ServiceException("未配置文件存储方式");
    }

    @Override
    public boolean deleteByFileName(String fileName) {
        throw new ServiceException("未配置文件存储方式");
    }

    @Override
    public InputStream getFileInputstream(String fileName) {
        throw new ServiceException("未配置文件存储方式");
    }

    @Override
    public String upload(FileUpload file) {
        throw new ServiceException("未配置文件存储方式");
    }
}
