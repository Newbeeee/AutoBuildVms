package com.example.autobuild.demo.service.task;

import com.example.autobuild.demo.util.ServerManager;
import org.openstack4j.model.compute.Server;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CreateVmHelper {
    public static Server createVm(String name, String flavorId, String imageId,
                           List<String> networks,
                           String securityGroup,
                           String keypairName, ServerManager serverManager) {

        Server server = null;
        try {
            server = serverManager.createServer(name, flavorId, imageId, networks, securityGroup, keypairName);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("创建虚拟机阶段出错: " + e.getMessage());
        }


        Server result = null;

        try {
            /**
             * 启动虚拟机并等待状态为active，最多等待2分钟
             * 如果2分钟还没创建好，每隔30秒请求一次
             * 直到状态为active
             * 这里还需要捕获一些异常，如资源不够，配额不够等
             * 但暂时还没遇到
             */
            result = serverManager.waitForServerStatus(server.getId(), Server.Status.ACTIVE, 2, TimeUnit.MINUTES);
            while (result.getStatus() == null) {
                result = serverManager.waitForServerStatus(server.getId(), Server.Status.ACTIVE, 30, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("等待虚拟机运行状态出错: " + e.getMessage());
            return null;
        }
        return result;
    }
}
