package online.zust.qcqcqc.services.module.attachment.handler;

import online.zust.qcqcqc.services.module.attachment.handler.impl.LocalStorageHandler;
import online.zust.qcqcqc.services.module.attachment.handler.impl.MinioStorageHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author qcqcqc
 * Date: 2024/4/12
 * Time: 11:45
 */
@Configuration
@Import({
        LocalStorageHandler.class,
        MinioStorageHandler.class,
        DefaultHandlerConfig.class
})
public class HandlerInject {
}
