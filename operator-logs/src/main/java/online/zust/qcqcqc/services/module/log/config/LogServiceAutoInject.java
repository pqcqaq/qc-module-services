package online.zust.qcqcqc.services.module.log.config;

import online.zust.qcqcqc.services.module.log.aspect.LoggingAspect;
import online.zust.qcqcqc.services.module.log.common.RequestMetadata;
import online.zust.qcqcqc.services.module.log.service.impl.LogServiceImpl;
import online.zust.qcqcqc.services.module.log.utils.SystemLogger;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author qcqcqc
 * Date: 2024/4/8
 * Time: 21:45
 */
@Configuration
@Import({
        LoggingAspect.class,
        LogServiceImpl.class,
        RequestMetadata.class,
})
public class LogServiceAutoInject {
}
