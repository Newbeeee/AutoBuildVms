package com.example.autobuild.demo.util;

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ServerManager {

    private OSClient.OSClientV3 os;

    public ServerManager(OSClient.OSClientV3 os) {
        this.os = os;
    }

    public Server createServer(String name, String flavorId, String imageId,
                               List<String> networks,
                               String securityGroup,
                               String keypairName) {
        ServerCreate sc = Builders.server()
                .name(name)
                .addSecurityGroup(securityGroup)
                .flavor(flavorId)
                .image(imageId)
                .networks(networks)
                .keypairName(keypairName)
                .build();

        Server server = null;
        try {
            server = os.compute().servers().boot(sc);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("创建虚拟机出错: " + e.getMessage());
            return null;
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

            result = os.compute().servers().waitForServerStatus(server.getId(), Server.Status.ACTIVE, 2, TimeUnit.MINUTES);
            while (result.getStatus() == null) {
                result = os.compute().servers().waitForServerStatus(server.getId(), Server.Status.ACTIVE, 30, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("创建虚拟机出错: " + e.getMessage());
            return null;
        }
        return result;
    }

    public boolean deleteServer(String serverId) {
        ActionResponse res = os.compute().servers().delete(serverId);
        return res.isSuccess();
    }
}
