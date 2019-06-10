package com.example.autobuild.demo.service.task;

import com.example.autobuild.demo.util.ServerManager;
import com.example.autobuild.demo.util.ThreadScopeOSClient;
import org.openstack4j.api.OSClient;

import java.util.concurrent.CountDownLatch;

public class DeleteVmTask implements Runnable {

    CountDownLatch latch;
    String vmId;
    public int num;

    public DeleteVmTask(CountDownLatch latch, String vmId, int num) {
        this.latch = latch;
        this.vmId = vmId;
        this.num = num;
    }

    @Override
    public void run() {
        OSClient.OSClientV3 os = ThreadScopeOSClient.getThreadInstance();
        try {
            ServerManager serverManager = new ServerManager(os);
            boolean result = serverManager.deleteServer(vmId);
            if (result) {
                int number = num + 1;
                System.out.println("删除第" + number + "个虚拟机成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }
    }
}
