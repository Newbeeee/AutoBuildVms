package com.example.autobuild.demo.service.impl;

import com.example.autobuild.demo.common.Response;
import com.example.autobuild.demo.service.CleanVmsService;
import com.example.autobuild.demo.service.task.DeleteVmTask;
import com.example.autobuild.demo.util.ThreadScopeOSClient;
import com.example.autobuild.demo.util.ServerManager;
import org.openstack4j.api.OSClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

@Service
public class CleanVmsServiceImpl implements CleanVmsService {


    private ThreadPoolExecutor threadPool;

    @Override
    public Response cleanVmById(String id) {
        if (id == null) {
            return new Response().success();
        }
        OSClient.OSClientV3 os = ThreadScopeOSClient.getThreadInstance();
        ServerManager serverManager = new ServerManager(os);
        boolean result = serverManager.deleteServer(id);
        return result ? new Response().success() : new Response().failure();
    }

    @Override
    public void cleanVmsByIds(List<String> ids) throws Exception {
        if (ids == null || ids.size() == 0) {
            throw new Exception("列表为空");
        }

        int total = ids.size();
        int threadCount = total < 10 ? total : 10;
        int num = 0;

        threadPool = new ThreadPoolExecutor(threadCount, threadCount, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(5));
        while (num < total) {
            CountDownLatch latch = new CountDownLatch(threadCount);
            for (int i = 0; i < threadCount; i++) {
                if (ids.get(num) != null) {
                    DeleteVmTask task = new DeleteVmTask(latch, ids.get(num), num);
                    try {
                        threadPool.execute(task);
                    } catch (RejectedExecutionException e) {
                        System.out.println("线程池已停止，任务：" + task.num + "被拒绝");
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
            }
        }

        //本次删除完成
        System.out.println("本次删除完成");
    }

    @Override
    public void stopCleaning() {
        if (threadPool != null) {
            threadPool.shutdown();
        }
    }
}
