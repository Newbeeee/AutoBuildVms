package com.example.autobuild.demo.service.impl;

import com.example.autobuild.demo.common.Response;
import com.example.autobuild.demo.common.ResponseStatusEnum;
import com.example.autobuild.demo.service.AutoBuildService;
import com.example.autobuild.demo.service.CleanVmsService;
import com.example.autobuild.demo.service.KeepCreatedVmsService;
import com.example.autobuild.demo.service.task.CreateVmTask;
import com.example.autobuild.demo.service.task.OnCreateVmResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class AutoBuildServiceImpl implements AutoBuildService, OnCreateVmResponse {

    public static final String HUNDRED_VM_LIST = "hundred_vm_list";

    @Autowired
    KeepCreatedVmsService keepCreatedVmsService;

    @Autowired
    CleanVmsService cleanVmsService;

    private ThreadPoolExecutor threadPool;
    private boolean createFailFlag = false;

    @Override
    public Response createVms(int threadCount, int totalVms, int startIndex) {
        createFailFlag = false;
        if (threadCount <= 0 || totalVms <= 0 || startIndex <= 0) {
            return new Response().failure(ResponseStatusEnum.BAD_PARAMETER);
        }

        int mThreadCount = threadCount;
        int mTotalVms = totalVms;
        int numId = startIndex;

        int count = 1;
        int times = 1;

        threadPool = new ThreadPoolExecutor(mThreadCount, mThreadCount, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(5));
        while (count < mTotalVms) {
            System.out.println("第 " + times + " 轮创建");
            /**
             * 判断剩余的任务数，比如一共 10 个任务，第一轮创建了 6 个
             * 此时 count = 7
             * 剩余任务数 last = 10 - 7 + 1 = 4
             * 如果剩余任务数小于并发线程数，那么只需提交剩余任务数次数的任务
             * 该次数记为 newThreadCount
             */
            int last = mTotalVms - count + 1;
            //如果剩余任务数
            int newThreadCount = last < threadCount ? last : threadCount;
            CountDownLatch latch = new CountDownLatch(newThreadCount);
            boolean latchAwait = true;
            for (int i = 0; i < newThreadCount; i++) {
                CreateVmTask mission = new CreateVmTask(latch, numId, this);
                try {
                    threadPool.execute(mission);
                } catch (RejectedExecutionException e) {
                    System.out.println("线程池已停止，任务：" + mission.numId + "被拒绝");
                    latchAwait = false;
                    break;
                }

                numId++;
                count++;
            }

            if (latchAwait) {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                times++;
            }


            if (threadPool.isShutdown()) {
                System.out.println("线程池已停止，停止创建虚拟机");
                createFailFlag = true;
                break;
            }
        }

        //可能会有创建失败的
        threadPool.shutdown();

        if (createFailFlag) {
            System.out.println("有虚拟机创建失败");
            return new Response().failure();
        } else {
            System.out.println("全部创建完成");
            return new Response().success();
        }
    }

    @Override
    public Response stopCreatingVms() {
        if (threadPool != null) {
            threadPool.shutdown();
            System.out.println("停止线程池");
        } else {
            System.out.println("线程池已停止");
        }
        return new Response().success();
    }

    @Override
    public Response cleanVms(int totalVms, int onceATime) {
        int num = 0;
        List<String> vmIdList = new ArrayList<>();
        String lastVmId = "";
        while (num < totalVms) {
            int newThreadCount = totalVms < onceATime ? totalVms : onceATime;
            for (int i = 0; i < newThreadCount; i++) {
                String vmId = keepCreatedVmsService.popVm(HUNDRED_VM_LIST);
                lastVmId = vmId;
                if (vmId == null) {
                    System.out.println("虚拟机列表已无缓存虚拟机");
                    break;
                }
                vmIdList.add(vmId);
                num++;
            }
            try {
                cleanVmsService.cleanVmsByIds(vmIdList);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            vmIdList.clear();
            if (lastVmId == null) {
                break;
            }
        }

        //全部删除完成，可能会有残余
        System.out.println("全部删除完成");
        //关闭清理虚拟机的线程池
        cleanVmsService.stopCleaning();
        return new Response().success();
    }

    @Override
    public void onFailure() {
        createFailFlag = true;
    }

    @Override
    public void onSuccess(String serverId) {
        keepCreatedVmsService.pushVm(HUNDRED_VM_LIST, serverId);
    }
}
