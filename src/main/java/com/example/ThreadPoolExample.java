package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.*;

/**
 * 该方法用于测试ThreadPoolExecutor接口，理解线程池工作原理
 */
public class ThreadPoolExample {

    // 使用logger代替打印
    public static final Logger logger = LogManager.getLogger(ThreadPoolExample.class.getName());

    // 创建一个ThreadPoolExecutor接口的 固定线程数量的线程池，线程数量为2
//    static ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
//    ExecutorService executor = Executors.newSingleThreadExecutor();
//    ExecutorService executor = Executors.newCachedThreadPool();

    private static BlockingQueue<Runnable> LinkedBlockingQueue = new LinkedBlockingQueue<>(6);
    static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 5, 2, TimeUnit.SECONDS, LinkedBlockingQueue, new ThreadPoolExecutor.DiscardOldestPolicy());

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 给线程池提交第一个Runnable任务，此时线程池给此任务分配一个线程
        threadPool.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });


        // 在提交的第一个任务没有执行完之前，给线程池提交第二个任务，此时线程池给该线程分配一个线程
        threadPool.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // 在提交的前两个线程都没有被线程池执行完前，给线程池提交第三个任务，超出线程池线程的数量，此时该任务被放入等待队列中，
        // future 获取执行结果  Runnable任务返回结果为null
        Future<?> future = threadPool.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            threadPool.execute(() -> {
                try {
                    logger.info("Task " + finalI + " is executing;");
                    Thread.sleep(1000);
                    logger.info("Active: " + threadPool.getActiveCount());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        // 线程池中线程的数量
        logger.info("PoolSize: " + threadPool.getPoolSize());
        // 线程池中正在执行任务的线程数
        logger.info("Active: " + threadPool.getActiveCount());
        // 该线程池等待队列中的等待任务数
        logger.info("Queue: " + threadPool.getQueue().size());

        // 通过future.get() 获取future的返回结果
//        logger.info(future.get());

        // 关闭线程池
        threadPool.shutdown();
    }
}
