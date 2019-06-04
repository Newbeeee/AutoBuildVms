package com.example.autobuild.demo.service.impl;

import com.example.autobuild.demo.service.RedisService;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service("redisService")
public class RedisServiceImpl implements RedisService {


    @Resource
    private RedisTemplate<String, ?> redisTemplate;

    @Override
    public boolean set(String key, String value) {
        boolean result = redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            redisConnection.set(Objects.requireNonNull(serializer.serialize(key)), serializer.serialize(value));
            return true;
        });
        return result;
    }

    @Override
    public String get(String key) {
        String result = redisTemplate.execute((RedisCallback<String>) redisConnection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            byte[] value = redisConnection.get(Objects.requireNonNull(serializer.serialize(key)));
            return serializer.deserialize(value);
        });
        return result;
    }

    @Override
    public boolean expire(String key, long expire) {
        return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    @Override
    public boolean remove(String key) {
        boolean result = redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            redisConnection.del(key.getBytes());
            return true;
        });
        return result;
    }

    @Override
    public boolean rightPush(String key, String value) {
        boolean result = redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            redisConnection.rPush(serializer.serialize(key), serializer.serialize(value));
            return true;
        });
        return result;
    }

    @Override
    public String rightPop(String key) {
        String result = redisTemplate.execute((RedisCallback<String>) redisConnection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            byte[] value = redisConnection.rPop(serializer.serialize(key));
            return serializer.deserialize(value);
        });
        return result;
    }
}
