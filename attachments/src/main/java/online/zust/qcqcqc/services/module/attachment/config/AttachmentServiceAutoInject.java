package online.zust.qcqcqc.services.module.attachment.config;

import lombok.extern.slf4j.Slf4j;
import online.zust.qcqcqc.services.module.attachment.handler.HandlerInject;
import online.zust.qcqcqc.services.module.attachment.service.impl.AttachmentsServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author qcqcqc
 * Date: 2024/4/12
 * Time: 11:44
 */
@Configuration
@Import({
        MinioConfig.class,
        HandlerInject.class,
        AttachmentsServiceImpl.class
})
@Slf4j
public class AttachmentServiceAutoInject {
    static {
        log.info("AttachmentService AutoInjected");
    }
}
