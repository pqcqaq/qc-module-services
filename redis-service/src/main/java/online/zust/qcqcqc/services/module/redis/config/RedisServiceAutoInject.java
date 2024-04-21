package online.zust.qcqcqc.services.module.redis.config;

import online.zust.qcqcqc.services.module.redis.listener.QcKeyDeleteEvent;
import online.zust.qcqcqc.services.module.redis.listener.QcKeyExpiredEvent;
import online.zust.qcqcqc.services.module.redis.listener.QcKeyUpdateEvent;
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
        QcKeyDeleteEvent.class,
        QcKeyExpiredEvent.class,
        QcKeyUpdateEvent.class
})
public class RedisServiceAutoInject {
}
