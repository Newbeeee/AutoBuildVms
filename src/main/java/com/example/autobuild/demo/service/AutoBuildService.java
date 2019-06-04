package com.example.autobuild.demo.service;

import com.example.autobuild.demo.common.Response;

public interface AutoBuildService {
    Response createVms(int threadCount, int totalVms, int startIndex);
    Response stopCreatingVms();
    Response cleanVms(int totalVms, int onceATime);
}
