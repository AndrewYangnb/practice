package com.redisson;

import com.util.RedissonAdapter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redisson.RedissonMultiLock;
import org.redisson.RedissonRedLock;
import org.redisson.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LocksExamples {

    private static final Logger logger = LogManager.getLogger("LocksExamples");

    //
    public static void main(String[] args) throws InterruptedException {
        // 提供了管理一个过多个异步任务终止的方法，可以生成Future跟踪任务进度
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // RCountDownLatch 闭锁 实例化一个RCountDownLatch对象
        final RCountDownLatch latch = RedissonAdapter.get("lock").getCountDownLatch("Test-countdownLatch");
        // 给latch赋值为1
        latch.trySetCount(1);

        // 在将来的某个时间执行给定的任务
        executor.execute(() -> {
            try {
                // 阻塞等待latch为零，或者时间超时
                logger.info("I am executing...");
                latch.await(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        // 允许执行完当前任务，然后关闭；
        executor.shutdown();
        // 在shutdown() 执行后，执行该命令发生阻塞，直到executor中的所有任务都执行完成，可以设置过期时间
        logger.info("The work in the executor is done? " + executor.awaitTermination(10, TimeUnit.SECONDS));


        // FairLock
        RLock rLock = RedissonAdapter.get("lock").getFairLock("Test-FairLock");

        // 设置一个常数，用来设置thread的数量
        int size = 10;
        // 创建一个列表，用来放线程
        List<Thread> threads = new ArrayList<>();
        // 循环创建线程
        for (int i = 0; i < size; i++) {
            final int j = i;
            // 写run方法，规定线程的执行
            Thread t = new Thread(() -> {
                // 获得锁
                rLock.lock();
                logger.info("thread " + j + " is already get the lock ");
                // 释放锁
                rLock.unlock();
            });
            // 创建的线程放入 threads列表
            threads.add(t);
        }
        // for循环执行刚才创建的线程
        for (Thread thread : threads) {
            // 执行线程
            thread.start();
            // 最多等待 5 个毫秒让该线程终止
            logger.info("Thread is running...");
            thread.join(5);
            logger.info("Thread is finished");
        }

        for (Thread thread : threads) {
            // 等待线程死掉
            thread.join();
        }


        // RLock
        RLock lock = RedissonAdapter.get("lock").getLock("lock");
        // 获得锁，获得时间 2s
        lock.lock(2, TimeUnit.SECONDS);

        // 创建一个线程
        // 如果线程是通过Runnable构造的，则执行Runnable中的run方法，否则，该函数不执行任何操作直接返回
        Thread t1 = new Thread(() -> {
            // 新建锁
            RLock lock1 = RedissonAdapter.get("lock").getLock("lock");
            // 线程获得锁
            lock1.lock();
            // 线程释放锁
            lock1.unlock();
        });
        // 启动线程
        t1.start();
        // 等待线程结束
        t1.join();


        // MultiLock
        // 创建3个锁
        RLock lock2 = RedissonAdapter.get("lock").getLock("lock2");
        RLock lock3 = RedissonAdapter.get("lock").getLock("lock3");
        RLock lock4 = RedissonAdapter.get("lock").getLock("lock4");

        // 起一个线程
        Thread t2 = new Thread(() -> {
            // 创建一个MultiLock
            RedissonMultiLock multiLock = new RedissonMultiLock(lock2, lock3, lock4);
            // 给当前线程上锁
            multiLock.lock();

            try {
                logger.info("Thread is sleeping...");
                // 该线程sleep
                Thread.sleep(3000);
                logger.info("Thread is active");

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // MultiLock释放锁
            multiLock.unlock();
        });
        // 运行t2线程
        t2.start();
        // 等待该线程运行完成
        logger.info(t2.isAlive());
        t2.join();
        logger.info(t2.isAlive());

        // PermitExpirableSemaphore —— 允许过期信号量
        // 实例化一个允许过期信号量实例
        RPermitExpirableSemaphore s = RedissonAdapter.get("lock1").getPermitExpirableSemaphore("test-permit");
        // 尝试设置许可证的数量 , 设置成功返回true，否则返回false
        // 此处会出现设置不成功
        boolean a = s.trySetPermits(1);
        logger.info(a);
        // 尝试使用已定义的租约时间获取当前可用的许可证并返回其 ID。 如有必要，等待定义的 waitTime，直到许可可用。
        String permitId = s.tryAcquire(1, 2, TimeUnit.SECONDS);

        // 起一个线程
        Thread t3 = new Thread(() -> {
            // 实例化一个允许过期信号量
            RPermitExpirableSemaphore s1 = RedissonAdapter.get("lock1").getPermitExpirableSemaphore("test-permit");

            try {
                // 获取许可证并返回其Id
                String permitId1 = s1.acquire();
                logger.info("测试");
                // 通过Id释放许可证，如果许可证已被释放或不存在，抛出异常；
                s1.release(permitId1);
                // 捕获异常
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 启动线程
        t3.start();
        // 等待线程执行完毕
        t3.join();

        // 尝试释放许可证
        s.tryRelease(permitId);
        logger.info("permitId is released.");


        // RReadWriteLock —— 读写锁
        // 新建一个读写锁实例
        final RReadWriteLock readWriteLock = RedissonAdapter.get("lock1").getReadWriteLock("Test-ReadWriteLock");
        // 尝试获取写锁，如果锁可用，则获取锁，如果锁不可用则返回false
        logger.info("The writeLock is locked? " + readWriteLock.writeLock().tryLock());

        // new一个线程
        Thread t4 = new Thread(() -> {
            // 返回读锁
            RLock r = readWriteLock.readLock();
            // 读锁上锁
            r.lock();

            try {
                logger.info("Thread t4 is sleeping...");
                // 线程sleep 1000 毫秒
                Thread.sleep(1000);
                logger.info("Thread t4 is active");
                // 捕获中断异常，当线程等待、休眠或以其他方式被占用，并且线程在活动之前或期间被中断时抛出。
            } catch (InterruptedException e) {
                // 将此异常打印到标准错误流
                e.printStackTrace();
            }
            // 读锁解锁
            r.unlock();
        });

        logger.info("is there work?");
        // 启动线程
        t4.start();
        // 等待线程挂掉
        t4.join(1000);

        // 写锁解锁
        readWriteLock.writeLock().unlock();
        logger.info("The writeLock is unlock.");


        // RedLock-红锁 - 联锁的子类，给不同的节点加锁，超过半数算加锁成功
        // 从节点lock1 获取锁
        RLock lock1 = RedissonAdapter.get("lock1").getLock("red-lock1");
        // 从节点lock1 获取锁
        RLock lock5 = RedissonAdapter.get("lock1").getLock("red-lock5");
        // 从节点lock2 获取锁
        RLock lock6 = RedissonAdapter.get("lock2").getLock("red-lock6");

        // new 一个线程
        Thread t5 = new Thread(() -> {
            // 给lock2 节点的锁上锁
            lock6.lock();
            logger.info("lock6 locked;");
        });
        // 线程启动
        t5.start();
        // 等待线程结束
        t5.join();

        // new 一个线程
        Thread t6 = new Thread(() -> {
            // 该线程实例化一个 RedLock
            RedissonMultiLock redLock = new RedissonRedLock(lock1, lock5, lock6);
            // RedLock 上锁
            redLock.lock();

            try {
                // 该线程sleep 3000 ms
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // RedLock解锁
            redLock.unlock();
        });
        // 启动线程
        t6.start();
        // 最多等待1000ms让该线程终止
        t6.join(1000);

        // 独立于其状态解锁锁
        lock6.forceUnlock();


        // Semaphore-信号量
        RSemaphore semaphore =RedissonAdapter.get("lock").getSemaphore("test-semaphore");
        // 设置许可证，设置成功返回True，已经被设置过了返回false
        semaphore.trySetPermits(5);
        logger.info("semaphore setPermit: " + semaphore.trySetPermits(5));
        // 获得指定数量得许可，
        semaphore.acquire(3);

        // new 一个线程
        Thread t7 = new Thread(() -> {
            RSemaphore semaphore1 = RedissonAdapter.get("lock").getSemaphore("test-semaphore");
            // 释放一个许可，可用许可增加一
            semaphore1.release();
            // 释放一个许可，可用许可增加一
            semaphore1.release();
        });
        // 启动线程
        t7.start();
        // 等待线程执行结束
        t7.join();

        RedissonAdapter.get("lock").shutdown();
        RedissonAdapter.get("lock1").shutdown();
        RedissonAdapter.get("lock2").shutdown();

    }

}
