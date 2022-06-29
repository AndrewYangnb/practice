package com.example;


import io.reactivex.rxjava3.internal.schedulers.NewThreadWorker;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/*多个线程互相拿着对方需要的共享资源，形成互相等待（僵持住）的情况。*/
public class DeadLock {


    public static ReentrantLock lock = new ReentrantLock();

    public static Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {

        Run run = new Run(0, "小明");
        Run run1 = new Run(1, "小红");

        new Thread(run).start();
        new Thread(run1).start();
        Thread.sleep(5200);

    }
    static class ChopSticks {
        ChopSticks() {}
    }

    static class Bowl {
        Bowl() {}
    }

    static class Run implements Runnable{
        static ChopSticks chopSticks = new ChopSticks();
        static Bowl bowl = new Bowl();

        private int id;
        private String name;

        Run(int id, String name) {
            this.id = id;
            this.name = name;
        }
        @Override
        public void run() {
            try {
                eat();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public void eat() throws InterruptedException {
            if(id == 0) {
                synchronized (chopSticks) {
                    Thread.sleep(1000);
                    System.out.println(name + "已经拿取了筷子");
                    synchronized (bowl) {
                        Thread.sleep(1000);
                        System.out.println(name + "已经拿取了碗");
                    }
                }
            } else {
                synchronized (bowl) {
                    Thread.sleep(1000);
                    System.out.println(name + "已经拿取了碗");
                    synchronized (chopSticks) {
                        Thread.sleep(1000);
                        System.out.println(name + "已经拿取了筷子");
                    }
                }
            }
        }
    }


}
