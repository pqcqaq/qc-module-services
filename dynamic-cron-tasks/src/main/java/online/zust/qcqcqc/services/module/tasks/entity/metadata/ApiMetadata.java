package online.zust.qcqcqc.services.module.tasks.entity.metadata;

import lombok.Data;

import java.util.Map;

/**
 * @author qcqcqc
 * Date: 2024/4/25
 * Time: 下午10:15
 */
@Data
public class ApiMetadata implements CronTaskMetadata {
    private String url;
    private String method;
    private Map<String, String> headers;
    private String body;
    private String contentType;
}
