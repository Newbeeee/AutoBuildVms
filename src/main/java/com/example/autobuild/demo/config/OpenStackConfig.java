package com.example.autobuild.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties
@PropertySource("classpath:openstack.properties")
public class OpenStackConfig {
    @Value("${openstack.username}")
    private String userName;

    @Value("${openstack.password}")
    private String password;

    @Value("${openstack.projectId}")
    private String projectId;

    @Value("${openstack.domainName}")
    private String domainName;

    @Value("${openstack.endpoint}")
    private String endPoint;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getEndPoint() {
        return endPoint;
    }
}
