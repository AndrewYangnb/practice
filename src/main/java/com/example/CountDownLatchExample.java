package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchExample {

    // 使用Logger来模仿任务的执行
    public static final Logger logger = LogManager.getLogger(CountDownLatchExample.class.getName());

    public static void main(String[] args) {

        // 新建一个CountDownLatch，计数器设置为2
        final CountDownLatch latch = new CountDownLatch(2);

        // 执行任务A的线程
        Thread A = new Thread(() -> {
            try {
                logger.info(Thread.currentThread().getName() + "任务A正在执行...");
                Thread.sleep(1000);
                logger.info(Thread.currentThread().getName() + "任务A执行完毕!");

                // 此线程执行完毕，让CountDownLatch 减一
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // new第二个线程并启动
        Thread B = new Thread(() -> {
            try {
                logger.info(Thread.currentThread().getName() + "任务B正在执行...");
                Thread.sleep(1000);
                logger.info(Thread.currentThread().getName() + "任务B执行完毕!");

                // 此线程执行完毕，让CountDownLatch 减一
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread C = new Thread(() -> {
            try {
                // 此时C线程阻塞，等待CountDownLatch计数器减为零，然后开始执行。
                latch.await();
                logger.info(Thread.currentThread().getName() + "任务C正在执行...");
                Thread.sleep(1000);
                logger.info(Thread.currentThread().getName() + "任务C执行完毕!");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        try {
            A.start();
            B.start();
            C.start();

            A.join();
            B.join();
            C.join();
            logger.info("任务A、B、C全部执行完毕");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
