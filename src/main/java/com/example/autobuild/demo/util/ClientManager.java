package com.example.autobuild.demo.util;

import com.example.autobuild.demo.config.OpenStackConfig;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.openstack.OSFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientManager {

    @Autowired
    OpenStackConfig openStackConfig;

    ThreadLocal<OSClient.OSClientV3> osClientV3ThreadLocal = ThreadLocal.withInitial(() -> connect(
            "icloudtest",
            "icloudtest",
            "f7ddf06aab0e4651bc487d5fed1f8c4b",
            "default"));

    private synchronized OSClient.OSClientV3 connect(String username, String password,
                                                     String projectId, String domainName) {

        OSClient.OSClientV3 osClientV3 = OSFactory.builderV3()
                .endpoint("http://192.168.0.209:5000/v3")
                .credentials(username, password, Identifier.byName(domainName))
                .scopeToProject(Identifier.byId(projectId))
                .authenticate();
        return osClientV3;
    }

    public OSClient.OSClientV3 getOSClientV3() {
        return osClientV3ThreadLocal.get();
    }
}
