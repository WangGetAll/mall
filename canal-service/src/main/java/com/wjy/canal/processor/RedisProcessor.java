package com.wjy.canal.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisProcessor {
    @Autowired
    private RedisTemplate redisTemplate;
    public Object get(String key) {
        if (key == null) {
            throw new UnsupportedOperationException("key is null");
        }
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, Object value) {
        if (key == null) {
            throw new UnsupportedOperationException("key is null");
        }
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        if (key == null) {
            throw new UnsupportedOperationException("key is null");
        }
        if (timeout > 0) {
            redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
        } else {
            set(key, value);
        }
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
