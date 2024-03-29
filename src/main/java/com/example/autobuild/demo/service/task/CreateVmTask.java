package com.example.autobuild.demo.service.task;

import com.example.autobuild.demo.config.CreateVmConfigLoader;
import com.example.autobuild.demo.util.GetVmAddressUtil;
import com.example.autobuild.demo.util.ServerManager;
import com.example.autobuild.demo.util.ThreadScopeOSClient;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CreateVmTask implements Runnable {
    public CountDownLatch latch;
    public int numId;
    OnCreateVmResponse onCreateVmResponse;

    public CreateVmTask(CountDownLatch latch, int numId, OnCreateVmResponse onCreateVmResponse) {
        this.latch = latch;
        this.numId = numId;
        this.onCreateVmResponse = onCreateVmResponse;
    }

    @Override
    public void run() {
        /**
         * 不重新建立 client的话有坑，报错：
         * Unable to retrieve current session.
         * Please verify thread has a current session available.
         */
        OSClient.OSClientV3 os = ThreadScopeOSClient.getThreadInstance();
        ServerManager createVmManager = new ServerManager(os);

        try {
            System.out.println("开始创建虚拟机 " + numId);
            //此时，虚拟机状态为 ACTIVE，虚拟机实际创建完毕
            //也可能虚拟机为 null，创建失败
//            Server server = createVmManager.createServer("hundred-vm-" + numId,
//                    CreateVmConfigLoader.FLAVOR_ID,
//                    CreateVmConfigLoader.IMAGE_ID,
//                    Arrays.asList(CreateVmConfigLoader.NETWORKS),
//                    CreateVmConfigLoader.SECURITY_GROUP,
//                    CreateVmConfigLoader.KEYPAIR_NAME);
            Server server = CreateVmHelper.createVm("hundred-vm-" + numId,
                    CreateVmConfigLoader.FLAVOR_ID,
                    CreateVmConfigLoader.IMAGE_ID,
                    Arrays.asList(CreateVmConfigLoader.NETWORKS),
                    CreateVmConfigLoader.SECURITY_GROUP,
                    CreateVmConfigLoader.KEYPAIR_NAME,
                    createVmManager);

            if (server == null) {
                //createFailFlag = true;
                onCreateVmResponse.onFailure();
            } else {
                //redisService.rightPush(HUNDRED_VM_LIST, server.getId());
                onCreateVmResponse.onSuccess(server.getId());

                //获取虚拟机的 ip 地址并打印
                List<String> address = GetVmAddressUtil.getVmAddress(server);
                String firstAddress = address.size() > 0 ? address.get(0) : "无地址";
                System.out.println(System.currentTimeMillis() + " ip地址 ：" + firstAddress + " 虚拟机名 ：" + server.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }
    }
}
