package com.example.autobuild.demo.service.task;

import com.example.autobuild.demo.util.ServerManager;
import com.github.rholder.retry.*;
import com.google.common.base.Predicates;
import org.openstack4j.model.compute.Server;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CreateVmHelper {


    public static Server createVm(String name, String flavorId, String imageId,
                                  List<String> networks,
                                  String securityGroup,
                                  String keypairName, ServerManager serverManager) {

        Server server = null;

        // 第一次创建虚拟机
        Callable<Server> firstCreateCall = new Callable<Server>() {
            @Override
            public Server call() {
                return serverManager.createServer(name, flavorId, imageId, networks, securityGroup, keypairName);
            }
        };

        // 重试策略
        Retryer<Server> retryer = RetryerBuilder.<Server>newBuilder()
                // 如果发生异常则重试
                .retryIfException()
                // 如果得到的server 为null则重试
                .retryIfResult(Predicates.equalTo(null))
                // 每次等待10秒后再重试
                .withWaitStrategy(WaitStrategies.fixedWait(10, TimeUnit.SECONDS))
                // 一共重试3次
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .build();

        try {
            server = retryer.call(firstCreateCall);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (RetryException e) {
            System.out.println("创建虚拟机阶段出错: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        Server finalServer = server;
        // 等待虚拟机状态为 active
        Callable<Server> waitForActiveCall = new Callable<Server>() {
            @Override
            public Server call() {
                return serverManager.waitForServerStatus(finalServer.getId(), Server.Status.ACTIVE, 2, TimeUnit.MINUTES);
            }
        };

        Server result = null;

        try {
            result = retryer.call(waitForActiveCall);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (RetryException e) {
            System.out.println("等待虚拟机运行状态出错: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return result;
    }
}
