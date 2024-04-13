package online.zust.qcqcqc.services.module.redis.config;

import online.zust.qcqcqc.services.module.redis.listener.QcKeyDeletePublisher;
import online.zust.qcqcqc.services.module.redis.listener.QcKeyExpiredPublisher;
import online.zust.qcqcqc.services.module.redis.listener.QcKeyUpdatePublisher;
import online.zust.qcqcqc.services.module.redis.service.RedisServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author qcqcqc
 * Date: 2024/4/13
 * Time: 20:25
 */
@Configuration
@Import({
        RedisConfiguration.class,
        RedisServiceImpl.class,
        QcKeyDeletePublisher.class,
        QcKeyExpiredPublisher.class,
        QcKeyUpdatePublisher.class
})
public class RedisServiceAutoInject {
}
