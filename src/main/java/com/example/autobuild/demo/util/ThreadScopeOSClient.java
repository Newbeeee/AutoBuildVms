package com.example.autobuild.demo.util;

import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.openstack.OSFactory;


public class ThreadScopeOSClient {

    private ThreadScopeOSClient() {}

    public static OSClient.OSClientV3 getThreadInstance() {
        OSClient.OSClientV3 instance = threadClients.get();
        if (instance == null) {
            instance = connect("icloudtest",
                    "icloudtest",
                    "f7ddf06aab0e4651bc487d5fed1f8c4b",
                    "default");
            threadClients.set(instance);
        }
        return instance;
    }

    private static ThreadLocal<OSClient.OSClientV3> threadClients = new ThreadLocal<>();

    private static OSClient.OSClientV3 connect(String username, String password,
                                                     String projectId, String domainName) {

        OSClient.OSClientV3 osClientV3 = OSFactory.builderV3()
                .endpoint("http://192.168.0.209:5000/v3")
                .credentials(username, password, Identifier.byName(domainName))
                .scopeToProject(Identifier.byId(projectId))
                .authenticate();
        return osClientV3;
    }
}
