package online.zust.qcqcqc.services.module.redis.service;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;

/**
 * @author qcqcqc
 */
public interface RedisService {
    /**
     * 存入redis 返回是否成功
     *
     * @param key   键
     * @param value 值
     * @return 是否成功
     */
    boolean set(String key, Object value);

    /**
     * 设置带有过期时间的值,单位是ms 返回是否成功
     *
     * @param key                  键
     * @param value                值
     * @param timeoutMillionSecond 过期时间，单位是ms
     * @return 是否成功
     */
    boolean set(String key, Object value, long timeoutMillionSecond);

    /**
     * 从redis中取出
     *
     * @param key       键
     * @param valueType 值的类型
     * @param <T>       值的类型
     * @return 值
     */
    <T> T get(String key, Class<T> valueType);

    /**
     * 从redis中取出
     *
     * @param key           键
     * @param typeReference 值的类型
     * @param <T>           值的类型
     * @return 值
     */
    <T> List<T> get(String key, TypeReference<List<T>> typeReference);

    /**
     * 判断某个key是否存在
     *
     * @param key 键
     * @return 是否存在
     */
    boolean exists(String key);

    /**
     * 删除指定key
     *
     * @param key 键
     */
    boolean delete(String key);


    /**
     * 设置map
     *
     * @param mapKey map的key
     * @param key    map中的key
     * @param value  map中的value
     */
    void setCountMap(String mapKey, String key, long value);

    /**
     * 从map中获取值
     *
     * @param mapKey map的key
     * @param key    map中的key
     * @return map中的value
     */
    long getCountFromMap(String mapKey, String key);

    /**
     * 从map中删除值
     *
     * @param mapKey map的key
     * @param key    map中的key
     */
    void incrementCountMap(String mapKey, String key);

    /**
     * 清空map
     *
     * @param mapKey map的key
     */
    void clearCountMap(String mapKey);

    /**
     * 获取map
     *
     * @param mapKey map的key
     * @return map
     */
    Map<String, Long> getCountMap(String mapKey);

    /**
     * 获取map的大小
     *
     * @param mapName map的key
     * @return map的大小
     */
    long getCountMapSize(String mapName);

    /**
     * 获取map
     *
     * @param mapKey    map的key
     * @param valueType map的value类型
     * @param <T>       map的value类型
     * @return map
     */
    <T> Map<String, T> getMap(String mapKey, Class<T> valueType);

    /**
     * 添加key到map
     *
     * @param mapKey map的key
     * @param key    key
     * @param value  value
     * @param <T>    value的类型
     * @return 是否成功
     */
    <T> boolean addKeyToMap(String mapKey, String key, T value);

    /**
     * 从map中获取key
     *
     * @param mapKey    map的key
     * @param key       key
     * @param valueType value的类型
     * @param <T>       value的类型
     * @return value
     */
    <T> T getKeyFromMap(String mapKey, String key, Class<T> valueType);

    /**
     * 从map中删除key
     *
     * @param mapKey map的key
     * @param key    key
     * @param <T>    value的类型
     * @return 是否成功
     */
    <T> boolean deleteKeyFromMap(String mapKey, String key);

    /**
     * 存入map
     *
     * @param key map的key
     * @param map map
     * @param <T> map的value类型
     */
    <T> void putMap(String key, Map<String, T> map);

    /**
     * 设置map的过期时间
     *
     * @param mapKey               map的key
     * @param timeoutMillionSecond 过期时间，单位是ms
     */
    void setTimeoutToMap(String mapKey, long timeoutMillionSecond);

    /**
     * 获取过期时间
     *
     * @param key 键
     * @return 过期时间
     */
    long getExpire(String key);

    /**
     * 获取过期时间的时间毫秒值
     *
     * @param key 键
     * @return 过期时间的毫秒值
     */
    long getTimeoutAt(String key);

    /**
     *  获取redisTemplate
     * @return redisTemplate
     */
    RedisTemplate<String, String> getRedisTemplate();
}
