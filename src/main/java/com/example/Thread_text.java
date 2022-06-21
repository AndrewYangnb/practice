package com.example;

public class Thread_text {

    private int NUM_WORKERS;

    class work implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println("111.....");
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void worker(int worker) {
        NUM_WORKERS = worker;
        for(int i = 0; i < worker; i++) {
            String threadName = "Thread" + i;
            Thread t = new Thread(new work());
            t.setName(threadName);
            t.start();
        }
    }

    public static void main(String[] args) throws Exception {
        // 如果使用此种方法启动线程，则每次都是new一个新的线程
        Thread_text thread_text = new Thread_text();

        Thread t = new Thread() {
            @Override
            public void run() {
                System.out.println("11.....");
                System.out.println("当前线程: " + Thread.currentThread().getName());
            }
        };

        // 没启动之前，线程状态为NEW
        System.out.println(t.getState());
        t.start();
        System.out.println(t.getState());
        // 线程启动后，执行过程中，线程状态为RUNNABLE
        System.out.println("当前线程: " + Thread.currentThread().getName());
        t.join();
        // 线程执行完毕，线程状态为TERMINATED
        System.out.println(t.getState());
        Thread.sleep(2000);
        // 测试：一个线程不能被启动两次

        // 线程启动之前会检查线程的状态（是NEW才会启动线程），此时线程已经执行完毕，状态为TERMINATED，所以不会再次启动线程；
        // IllegalThreadStateException : 线程状态不合法
        t.start();
    }

}
