package online.zust.qcqcqc.services.module.attachment.handler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author qcqcqc
 * Date: 2024/5/7
 * Time: 下午11:18
 */
@Component
public class DefaultHandlerConfig {

    @Bean
    @ConditionalOnMissingBean(AttachmentsStorageHandler.class)
    public AttachmentsStorageHandler defaultHandler() {
        return new DefaultAttachmentsStorageHandler();
    }

}
