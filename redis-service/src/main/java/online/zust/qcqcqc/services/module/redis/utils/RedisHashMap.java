package online.zust.qcqcqc.services.module.redis.utils;

import online.zust.qcqcqc.services.module.redis.exception.RedisException;
import online.zust.qcqcqc.services.module.redis.service.RedisService;
import online.zust.qcqcqc.utils.utils.ProxyUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qcqcqc
 * Date: 2024/4/13
 * Time: 19:10
 */
@SuppressWarnings("unchecked")
public class RedisHashMap<T> {
    private static RedisService redisService;
    private final Class<T> valueType;
    private final HashMap<String, T> Data = new HashMap<>();
    private final String key;
    private long timeoutAt;

    /**
     * 获取redisService
     *
     * @return redisService
     */
    private static RedisService getRedisService() {
        if (redisService == null) {
            redisService = ProxyUtil.getBean(RedisService.class);
        }
        return redisService;
    }

    /**
     * 通过key创建，则从redis中同步数据
     *
     * @param key key
     */
    public RedisHashMap(String key, Class<T> clazz) {
        this.key = key;
        timeoutAt = getRedisService().getTimeoutAt(this.key);
        valueType = clazz;
        syncFromRedis();
    }

    /**
     * 通过key和map创建，将map存入redis
     *
     * @param key key
     * @param map map
     */
    public RedisHashMap(String key, Map<String, T> map) {
        // 获取map中元素的类型
        if (map == null || map.isEmpty()) {
            throw new RedisException("map is empty");
        }
        Class<?> aClass = map.values().iterator().next().getClass();
        valueType = (Class<T>) aClass;
        this.key = key;
        Data.putAll(map);
        Data.putAll(getRedisService().getMap(key, valueType));
        getRedisService().putMap(key, map);
        timeoutAt = getRedisService().getTimeoutAt(this.key);
    }

    /**
     * 增加
     *
     * @param key   key
     * @param value value
     */
    public void put(String key, T value) {
        if (isExpired()) {
            throw new RedisException("RedisHashMap is expired");
        }
        Data.put(key, value);
        getRedisService().addKeyToMap(this.key, key, value);
    }

    /**
     * 删除
     */
    public void remove(String key) {
        if (isExpired()) {
            throw new RedisException("RedisHashMap is expired");
        }
        Data.remove(key);
        getRedisService().deleteKeyFromMap(this.key, key);
    }

    /**
     * 查找
     */
    public T get(String key) {
        if (isExpired()) {
            throw new RedisException("RedisHashMap is expired");
        }
        T t = Data.get(key);
        if (t == null) {
            t = getRedisService().getKeyFromMap(this.key, key, valueType);
            if (t != null) {
                Data.put(key, t);
            }
        }
        return t;
    }

    /**
     * 获取所有
     */
    public Map<String, T> getAll() {
        if (isExpired()) {
            throw new RedisException("RedisHashMap is expired");
        }
        return Data;
    }

    /**
     * 获取大小
     */
    public int size() {
        if (isExpired()) {
            throw new RedisException("RedisHashMap is expired");
        }
        return Data.size();
    }

    /**
     * 同步
     */
    public void syncToRedis() {
        if (Data.isEmpty()) {
            getRedisService().delete(this.key);
        } else {
            getRedisService().putMap(this.key, Data);
        }
    }

    /**
     * 从redis中同步
     */
    public void syncFromRedis() {
        Data.clear();
        Data.putAll(getRedisService().getMap(this.key, valueType));
    }

    /**
     * 设置整个map的超时时间
     *
     * @param timeoutMillionSecond 超时时间
     */
    public void setTimeout(long timeoutMillionSecond) {
        getRedisService().setTimeoutToMap(this.key, timeoutMillionSecond);
        timeoutAt = System.currentTimeMillis() + timeoutMillionSecond;
    }

    /**
     * 判断是否过期
     *
     * @return 是否过期
     */
    public boolean isExpired() {
        if (timeoutAt <= 0) {
            return false;
        }
        return System.currentTimeMillis() > timeoutAt;
    }

}
