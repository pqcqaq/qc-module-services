package online.zust.qcqcqc.services.module.attachment.entity.dto;

import lombok.Data;

/**
 * @author pqcmm
 */
@Data
public class FileUpload {
    private String base64;
    private String fileName;
    private String fileType;
    private String tag;
}
