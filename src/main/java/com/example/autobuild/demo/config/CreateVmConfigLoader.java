package com.example.autobuild.demo.config;

import java.util.ResourceBundle;

public class CreateVmConfigLoader {
    private final static ResourceBundle resourceBundle =
            ResourceBundle.getBundle("auto_build");
    public final static String FLAVOR_ID =
            resourceBundle.getString("autoBuild.flavorId");
    public final static String IMAGE_ID =
            resourceBundle.getString("autoBuild.imageId");
    public final static String SECURITY_GROUP =
            resourceBundle.getString("autoBuild.securityGroup");
    public final static String KEYPAIR_NAME =
            resourceBundle.getString("autoBuild.keypairName");
    public final static String NETWORKS =
            resourceBundle.getString("autoBuild.networks");
}
