package com.example.netmanager;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//线程池管理类
public class ThreadPoolManager {
    private static ThreadPoolManager threadPoolManager = new ThreadPoolManager();

    public static ThreadPoolManager getInstance() {
        return threadPoolManager;
    }

    // 线程安全
    private LinkedBlockingQueue<Runnable> mQueue = new LinkedBlockingQueue<>();
    private DelayQueue<HttpTask> mDelay = new DelayQueue<>();
    //线程池
    private ThreadPoolExecutor mThreadPoolExecutor;

    private ThreadPoolManager() {
        mThreadPoolExecutor = new ThreadPoolExecutor(3, 6, 15,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                //线程没执行成功，返回到这里
                addTask(r);
            }
        });
        mThreadPoolExecutor.execute(coreThread);
        mThreadPoolExecutor.execute(delayThread);
    }

    public void addTask(Runnable runnable) {
        if (runnable != null) {
            try {
                mQueue.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addDelayTask(HttpTask runnable) {
        if (runnable != null) {
            runnable.setDelayTime(3000);
            mDelay.offer(runnable);
        }
    }

    // 创建核心线程 将队列中的请求拿出来 ，交给线程池处理
    public Runnable coreThread = new Runnable() {
        Runnable runnable = null;

        @Override
        public void run() {
            while (true) {
                try {
                    runnable = mQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mThreadPoolExecutor.execute(runnable);
            }
        }
    };


    public Runnable delayThread = new Runnable() {
        HttpTask ht = null;

        @Override
        public void run() {

            while (true) {
                try {
                    ht = mDelay.take();
                    if (ht.getRetryCount() < 3) {
                        mThreadPoolExecutor.execute(ht);
                        ht.setRetryCount(ht.getRetryCount() + 1);
                        Log.e("===重试机智===", ht.getRetryCount() + "  " + System.currentTimeMillis());
                    } else {
                        Log.e("===重试机智===", "放弃");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


}
