package online.zust.qcqcqc.services.module.redis.service;/*
 *文件名: RedisServiceImpl
 *创建者: myy
 *创建时间:2024/2/20 19:57
 *描述: 这是一个示例
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.zust.qcqcqc.services.exception.ServiceException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author myy
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public boolean set(String key, Object value) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    @Override
    public boolean set(String key, Object value, long timeoutMillionSecond) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue, timeoutMillionSecond, TimeUnit.MILLISECONDS);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    @Override
    public <T> T get(String key, Class<T> valueType) {
        try {
            String jsonValue = redisTemplate.opsForValue().get(key);
            return objectMapper.readValue(jsonValue, valueType);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public <T> List<T> get(String key, TypeReference<List<T>> typeReference) {
        String jsonValue = redisTemplate.opsForValue().get(key);
        if (jsonValue != null) {
            try {
                return objectMapper.readValue(jsonValue, typeReference);
            } catch (JsonProcessingException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    @Override
    public void setCountMap(String mapKey, String key, long value) {
        redisTemplate.opsForHash().put(mapKey, key, String.valueOf(value));
    }

    @Override
    public long getCountFromMap(String mapKey, String key) {
        String value = (String) redisTemplate.opsForHash().get(mapKey, key);
        if (value == null) {
            return 0;
        }
        return Long.parseLong(value);
    }

    @Override
    public void incrementCountMap(String mapKey, String key) {
        // 如果不存在key，则默认为0
        redisTemplate.opsForHash().increment(mapKey, key, 1);
    }

    @Override
    public void clearCountMap(String mapKey) {
        redisTemplate.delete(mapKey);
    }

    @Override
    public Map<String, Long> getCountMap(String mapKey) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(mapKey);
        Map<String, Long> result = new java.util.HashMap<>();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            result.put((String) entry.getKey(), Long.parseLong((String) entry.getValue()));
        }
        return result;
    }

    @Override
    public long getCountMapSize(String mapName) {
        return redisTemplate.opsForHash().size(mapName);
    }

    @Override
    public <T> Map<String, T> getMap(String mapKey, Class<T> valueType) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(mapKey);
        Map<String, T> result = new java.util.HashMap<>();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            try {
                result.put((String) entry.getKey(), objectMapper.readValue((String) entry.getValue(), valueType));
            } catch (Exception e) {
                log.error("getMap error", e);
                throw new ServiceException("getMap error");
            }
        }
        return result;
    }

    @Override
    public <T> boolean addKeyToMap(String mapKey, String key, T value) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForHash().put(mapKey, key, jsonValue);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    @Override
    public <T> T getKeyFromMap(String mapKey, String key, Class<T> valueType) {
        String jsonValue = (String) redisTemplate.opsForHash().get(mapKey, key);
        if (jsonValue == null) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonValue, valueType);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public <T> boolean deleteKeyFromMap(String mapKey, String key) {
        return redisTemplate.opsForHash().delete(mapKey, key) > 0;
    }

    @Override
    public <T> void putMap(String key, Map<String, T> map) {
        Map<String, String> stringMap = new java.util.HashMap<>();
        for (Map.Entry<String, T> entry : map.entrySet()) {
            try {
                stringMap.put(entry.getKey(), objectMapper.writeValueAsString(entry.getValue()));
            } catch (JsonProcessingException e) {
                log.error("putMap error", e);
                throw new ServiceException("putMap error");
            }
        }
        redisTemplate.opsForHash().putAll(key, stringMap);
    }

    @Override
    public void setTimeoutToMap(String mapKey, long timeoutMillionSecond) {
        redisTemplate.expire(mapKey, timeoutMillionSecond, TimeUnit.MILLISECONDS);
    }

    @Override
    public long getExpire(String key) {
        Long expire = redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
        if (expire == null) {
            return -1;
        }
        return expire;
    }

    @Override
    public long getTimeoutAt(String key) {
        long expire = getExpire(key);
        if (expire < 0) {
            return -1;
        }
        return expire + System.currentTimeMillis();
    }
}
