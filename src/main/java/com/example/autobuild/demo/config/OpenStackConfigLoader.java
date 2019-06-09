package com.example.autobuild.demo.config;

import java.util.ResourceBundle;

public class OpenStackConfigLoader {

    private final static ResourceBundle resourceBundle =
            ResourceBundle.getBundle("openstack");

    public final static String USER_NAME =
            resourceBundle.getString("openstack.user-name");
    public final static String PASSWORD =
            resourceBundle.getString("openstack.password");
    public final static String PROJECT_ID =
            resourceBundle.getString("openstack.project-id");
    public final static String DOMAIN_NAME =
            resourceBundle.getString("openstack.domain-name");
    public final static String ENDPOINT =
            resourceBundle.getString("openstack.endpoint");
}
