package com.example.autobuild.demo.service.impl;

import com.example.autobuild.demo.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisServiceImplTest {

    @Autowired
    RedisService redisService;

    @Test
    public void set() {
        redisService.set("autoBuild", "autoBuild");
    }

    @Test
    public void get() {
        assertEquals(redisService.get("autoBuild"), "autoBuild");
    }

    @Test
    public void remove() {
        redisService.remove("autoBuild");
        assertEquals(redisService.get("autoBuild"), null);
    }

    @Test
    public void rightPush() {
        redisService.rightPush("testList", "test1");
        redisService.rightPush("testList", "test2");
    }

    @Test
    public void rightPop() {
        assertEquals(redisService.rightPop("testList"), "test2");
    }
}