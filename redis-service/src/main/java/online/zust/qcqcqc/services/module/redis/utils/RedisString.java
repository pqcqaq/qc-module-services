package online.zust.qcqcqc.services.module.redis.utils;

import online.zust.qcqcqc.services.module.redis.service.RedisService;
import online.zust.qcqcqc.utils.utils.BeanConvertUtils;
import online.zust.qcqcqc.utils.utils.ProxyUtil;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

/**
 * @author qcqcqc
 * Date: 2024/5/12
 * Time: 下午10:41
 */
public class RedisString<T> {
    private static RedisService redisService;
    private final Class<T> valueType;
    private final Long timeoutAt;

    public T get() {
        String entity = getRedisTemplate().opsForValue().get(key);
        // 如果是java的基本类型，直接返回
        if (valueType.getTypeName().startsWith("java")) {
            return (T) entity;
        }
        return BeanConvertUtils.objectConvent(entity, valueType);
    }

    public String getKey() {
        return key;
    }

    private static RedisTemplate<String, String> getRedisTemplate() {
        if (redisService == null) {
            redisService = ProxyUtil.getBean(RedisService.class);
        }
        return redisService.getRedisTemplate();
    }

    private final String key;

    public RedisString(String key, Class<T> clazz) {
        this.key = key;
        this.valueType = clazz;
        String value1 = getRedisTemplate().opsForValue().get(key);
        if (value1 == null) {
            this.timeoutAt = -1L;
            return;
        }
        this.timeoutAt = getRedisTemplate().getExpire(key);
    }

    public Long strLen() {
        return getRedisTemplate().opsForValue().size(key);
    }

    public void set(T value) {
        getRedisTemplate().opsForValue().set(key, BeanConvertUtils.objectConvent(value, String.class));
    }

    public void set(T value, long timeoutMillionSecond) {
        getRedisTemplate().opsForValue().set(key, BeanConvertUtils.objectConvent(value, String.class), timeoutMillionSecond);
    }

    public Boolean isExist() {
        return getRedisTemplate().hasKey(key);
    }

    public void del() {
        getRedisTemplate().delete(key);
    }

    public void setTimeout(Long timeoutMillionSecond) {
        getRedisTemplate().expire(key, Duration.ofMillis(timeoutMillionSecond));
    }

    public Boolean isTimeout() {
        return this.timeoutAt != null && this.timeoutAt < 0;
    }

}
