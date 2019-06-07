package com.example.autobuild.demo.util;

import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.openstack.OSFactory;


public class ThreadScopeOSClient {

    private static ThreadLocal<OSClient.OSClientV3> threadClients = new ThreadLocal<>();

    private ThreadScopeOSClient() {
    }

    public static OSClient.OSClientV3 getThreadInstance() {
        OSClient.OSClientV3 instance = threadClients.get();
        if (instance == null) {
            instance = connect(OpenStackConfigLoader.USER_NAME,
                    OpenStackConfigLoader.PASSWORD,
                    OpenStackConfigLoader.PROJECT_ID,
                    OpenStackConfigLoader.DOMAIN_NAME);
            threadClients.set(instance);
        }
        return instance;
    }

    private static OSClient.OSClientV3 connect(String username, String password,
                                               String projectId, String domainName) {

        OSClient.OSClientV3 osClientV3 = OSFactory.builderV3()
                .endpoint(OpenStackConfigLoader.ENDPOINT)
                .credentials(username, password, Identifier.byName(domainName))
                .scopeToProject(Identifier.byId(projectId))
                .authenticate();
        return osClientV3;
    }
}
