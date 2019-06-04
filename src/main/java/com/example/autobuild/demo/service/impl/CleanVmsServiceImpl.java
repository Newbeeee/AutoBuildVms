package com.example.autobuild.demo.service.impl;

import com.example.autobuild.demo.common.Response;
import com.example.autobuild.demo.service.CleanVmsService;
import com.example.autobuild.demo.util.ClientManager;
import com.example.autobuild.demo.util.ServerManager;
import org.openstack4j.api.OSClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

@Service
public class CleanVmsServiceImpl implements CleanVmsService {

    private ClientManager clientManager = new ClientManager();
    private ExecutorService executorService;

    @Override
    public Response cleanVmById(String id) {
        if (id == null) {
            return new Response().success();
        }
        OSClient.OSClientV3 os = clientManager.getOSClientV3();
        ServerManager serverManager = new ServerManager(os);
        boolean result = serverManager.deleteServer(id);
        return result ? new Response().success() : new Response().failure();
    }

    @Override
    public Response cleanVmsByIds(List<String> ids) {
        if (ids == null || ids.size() == 0) {
            return new Response().success();
        }

        int total = ids.size();
        int threadCount = total < 10 ? total : 10;
        int num = 0;

        executorService = Executors.newFixedThreadPool(threadCount);
        while (num < total) {
            CountDownLatch latch = new CountDownLatch(threadCount);
            for (int i = 0; i < threadCount; i++) {
                if (ids.get(num) != null) {
                    DeleteVmMission mission = new DeleteVmMission(latch, ids.get(num), num);
                    try {
                        executorService.submit(mission);
                    } catch (RejectedExecutionException e) {
                        System.out.println("线程池已停止，任务：" + mission.num + "被拒绝");
                    } finally {
                        num++;
                    }
                } else {
                    System.out.print("已删除完虚拟机");
                }
            }

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
                latch.countDown();
                break;
            }
        }

        //本次删除完成
        System.out.println("本次删除完成");
        return new Response().success();
    }

    @Override
    public void stopCleaning() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    class DeleteVmMission extends Thread {

        CountDownLatch latch;
        String vmId;
        int num;

        public DeleteVmMission(CountDownLatch latch, String vmId, int num) {
            this.latch = latch;
            this.vmId = vmId;
            this.num = num;
        }

        @Override
        public void run() {
            OSClient.OSClientV3 os = clientManager.getOSClientV3();
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
}
