package com.example.autobuild.demo.service;

public interface RedisService {
    boolean set(String key, String value);

    String get(String key);

    boolean expire(String key, long expire);

    boolean remove(String key);

    boolean rightPush(String key, String value);

    String rightPop(String key);
}
