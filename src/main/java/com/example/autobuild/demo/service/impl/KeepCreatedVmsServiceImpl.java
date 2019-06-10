package com.example.autobuild.demo.service.impl;

import com.example.autobuild.demo.service.KeepCreatedVmsService;
import com.example.autobuild.demo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class KeepCreatedVmsServiceImpl implements KeepCreatedVmsService {

    @Autowired
    RedisService redisService;

    @Override
    public void pushVm(String listId, String vmId) {
        redisService.rightPush(listId, vmId);
    }

    @Override
    public String popVm(String listId) {
        return redisService.rightPop(listId);
    }
}
