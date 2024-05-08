package online.zust.qcqcqc.services.config;

import online.zust.qcqcqc.services.async.AsyncConfig;
import online.zust.qcqcqc.services.entity.checker.CheckHandler;
import online.zust.qcqcqc.services.utils.SpElParser;
import online.zust.qcqcqc.services.utils.config.MbpUtilsAutoInject;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author qcqcqc
 * Date: 2024/4/10
 * Time: 15:44
 */
@Configuration
@Import({
        AsyncConfig.class,
        SpElParser.class,
        CheckHandler.class,
        MbpMetaObjectHandler.class,
        MbpEnhanceMapperScannerConfigurer.class,
        MbpUtilsAutoInject.class,
})
public class CommonDependencyAutoInject {
}
