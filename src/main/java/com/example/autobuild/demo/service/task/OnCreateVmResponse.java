package com.example.autobuild.demo.service.task;

public interface OnCreateVmResponse {
    void onFailure();
    void onSuccess(String serverId);
}
