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
        Server server = os.compute().servers().boot(sc);
        return server;
    }

    public boolean deleteServer(String serverId) {
        ActionResponse res = os.compute().servers().delete(serverId);
        return res.isSuccess();
    }

    public Server waitForServerStatus(String serverId, Server.Status status, int time, TimeUnit timeUnit) {
        return os.compute().servers().waitForServerStatus(serverId, status, time, timeUnit);
    }
}
