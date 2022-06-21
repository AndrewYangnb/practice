package com.thread;

public class simpleThreads {

        // Display a message, preceded by
        // the name of the current thread
        // 定义函数，用来打印格式化的消息    ThreadName: Message
        static void threadMessage(String message) {
            String threadName =
                    Thread.currentThread().getName();
            System.out.format("%s: %s%n",
                    threadName,
                    message);
        }

        // 该类实现Runnable接口，用来定义线程的操作。
        private static class MessageLoop
                implements Runnable {
            public void run() {
                String[] importantInfo = {
                        "Mares eat oats",
                        "Does eat oats",
                        "Little lambs eat ivy",
                        "A kid will eat ivy too"
                };
                try {
                    for (String s : importantInfo) {
                        // Pause for 4 seconds
                        Thread.sleep(4000);
                        // Print a message
                        threadMessage(s);
                    }
                } catch (InterruptedException e) {
                    threadMessage("I wasn't done!");
                }
            }
        }

        public static void main(String[] args)
                throws InterruptedException {

            // 定义MessageLoop线程过期时间，时长一个小时
            long patience = 1000 * 60 * 60;

            // 提示MessageLoop线程启动
            threadMessage("Starting MessageLoop thread");
            // startTime用来记录该MessageLoop线程启动时间，方便中断线程调用
            long startTime = System.currentTimeMillis();
            // 创建一个MessageLoop线程
            Thread t = new Thread(new MessageLoop());
            // 启动线程
            t.start();


            threadMessage("Waiting for MessageLoop thread to finish");

            // 如果线程MessageLoop没有执行完毕，一直循环。
            while (t.isAlive()) {
                threadMessage("Still waiting...");

                // 主线程等待t线程1s
                t.join(1000);

                // 如果线程t启动超过一个小时，线程t还处于Alive状态，则中断这个线程
                if (((System.currentTimeMillis() - startTime) > patience)
                        && t.isAlive()) {
                    threadMessage("Tired of waiting!");
                    // 中断MessageLoop线程
                    t.interrupt();
                    // 主线程等待线程t执行推出
                    t.join();
                }
            }
            // 主线程执行完毕，程序退出
            threadMessage("Finally!");
        }

}
