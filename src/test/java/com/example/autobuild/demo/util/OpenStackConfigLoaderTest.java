package com.example.autobuild.demo.util;

import com.example.autobuild.demo.config.OpenStackConfigLoader;
import org.junit.Test;

public class OpenStackConfigLoaderTest {

    @Test
    public void getUserName() {
        System.out.println(OpenStackConfigLoader.USER_NAME);
    }
}