package com.example.autobuild.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@ConfigurationProperties
@PropertySource("classpath:auto_build.properties")
public class AutoBuildConfig {
    @Value("${autoBuild.flavorId}")
    private String flavorId;

    @Value("${autoBuild.imageId}")
    private String imageId;

    @Value("${autoBuild.securityGroup}")
    private String securityGroup;

    @Value("${autoBuild.keypairName}")
    private String keypairName;

    @Value("#{'${autoBuild.networks}'.split(',')}")
    private List<String> networks;

    public String getFlavorId() {
        return flavorId;
    }

    public String getImageId() {
        return imageId;
    }

    public String getSecurityGroup() {
        return securityGroup;
    }

    public String getKeypairName() {
        return keypairName;
    }

    public List<String> getNetworks() {
        return networks;
    }
}
