package com.example.autobuild.demo.service;

import com.example.autobuild.demo.common.Response;

import java.util.List;

public interface CleanVmsService {
    Response cleanVmById(String id);

    void cleanVmsByIds(List<String> ids) throws Exception;

    void stopCleaning();
}
