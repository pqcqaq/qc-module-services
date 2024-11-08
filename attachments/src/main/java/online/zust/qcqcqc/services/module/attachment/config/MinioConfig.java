package online.zust.qcqcqc.services.module.attachment.config;

import io.minio.MinioClient;
import lombok.Data;
import online.zust.qcqcqc.services.module.attachment.handler.impl.MinioStorageHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pqcmm
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "storage.minio")
@ConditionalOnBean(MinioStorageHandler.class)
public class MinioConfig {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
