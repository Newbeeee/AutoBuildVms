package com.example.autobuild.demo.service;

public interface KeepCreatedVmsService {
    void pushVm(String listId, String vmId);
    String popVm(String listId);
}
