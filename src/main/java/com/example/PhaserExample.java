package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/**
 * 4名运动员，同时起跑，路程共分三个阶段，有三个运动员到达第一阶段点，则可以开始跑第二阶段；有2个运动员到达第二阶段点，则可以开始跑到终点；
 */
public class PhaserExample {

    // 创建一个线程池，线程数量可变；
    static ExecutorService executorService = Executors.newCachedThreadPool();

    // 用logger代替println
    public static final Logger logger = LogManager.getLogger(PhaserExample.class.getName());

    /**
     * 实现Runnable接口，定义线程执行逻辑
     */
    static class Run implements Runnable {
        public String athlete;
        private final Phaser phaser;

        // 构造函数，athlete为传入的运动员名字，线程启动时自动注册phaser
        public Run(Phaser phaser, String athlete) {
            this.phaser = phaser;
            this.athlete = athlete;
            phaser.register();
        }

        // 运动员跑阶段1
        public void runStep1() throws InterruptedException {
            Thread.sleep(1000);
            logger.info(Thread.currentThread().getName() + " 到达step1");
            // 跑到阶段点一，跑的慢的自动从phaser注销，睡1s，自己跑后面的
            if (phaser.getArrivedParties() > 2) {
                logger.info(Thread.currentThread().getName() + "exit phaser.");
                phaser.arriveAndDeregister();

//                Thread.currentThread().join(1000);
                Thread.sleep(1000);
            } else {
                // 跑的快的在step1等待
                phaser.arriveAndAwaitAdvance();
            }
        }

        // 仍然注册的线程继续跑阶段二，
        public void runStep2() throws InterruptedException {
            Thread.sleep(1000);
            logger.info(Thread.currentThread().getName() + " 到达step2");
            // 跑到阶段二点，跑的慢的注销，睡1s，自己跑后面的
            if (phaser.getArrivedParties() > 1) {
                logger.info(Thread.currentThread().getName() + "exit phaser.");
                phaser.arriveAndDeregister();

//                Thread.currentThread().join(1000);
                Thread.sleep(1000);
            } else {
                // 跑到阶段点，跑的快的在此等待
                phaser.arriveAndAwaitAdvance();
            }
        }

        // 跑阶段3，然后到达终点
        public void runStep3() throws InterruptedException {
            Thread.sleep(1000);
            logger.info(Thread.currentThread().getName() + " 到达终点");
            phaser.arriveAndAwaitAdvance();
        }

        // 线程的执行逻辑
        @Override
        public void run() {
            // 给当前线程设置名称  运动员名称
            Thread.currentThread().setName(athlete);
            logger.info(Thread.currentThread().getName() + " 准备就绪！");
            // 等待所有线程到达屏障
            phaser.arriveAndAwaitAdvance();

            // 线程跑第一阶段
            try {
                runStep1();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 线程跑第二阶段
            try {
                runStep2();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // 线程到达终点
            try {
                runStep3();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 新建Phaser并覆写onAdvance函数
        Phaser phaser = new Phaser() {
            // 覆写onAdvance函数，到达一次阶段点，打印提示消息：当前的阶段和在phaser中的线程数（parties数量）
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                logger.info("===============step-" + phase + "=====================" + registeredParties);
                return super.onAdvance(phase, registeredParties);
            }
        };

        executorService.submit(new Run(phaser,"小王"));
        executorService.submit(new Run(phaser,"小杨"));
        executorService.submit(new Run(phaser,"小李"));
        executorService.submit(new Run(phaser,"小张"));


        executorService.shutdown();

    }
}
