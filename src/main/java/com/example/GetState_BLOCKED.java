package com.example;

public class GetState_BLOCKED {
    public static void main(String[] args) {
        DemoThread demoThread = new DemoThread();

        Thread t1 = new Thread(demoThread);
        Thread t2 = new Thread(demoThread);

        t1.start();
        t2.start();

        // 等待线程启动并执行
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(t2.getState());
        System.exit(0);
    }
}

class DemoThread implements Runnable {

    @Override
    public void run() {
        commonResource();
    }

    // 临界资源，同一时间只能有一个线程进入此资源
    public synchronized void commonResource() {
        while(true) {
            // 线程掉入死循环，另一个线程尝试获取此资源
        }
    }
}