package com.example;

// 测试join方法中线程的状态
public class TextState implements Runnable {


    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            try {
                write();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + "执行到 " + i);
            }
        }

    public synchronized void write() throws InterruptedException {
        wait(100);
        System.out.println(Thread.currentThread().getName() + " hei");
    }
    public static void main(String[] args) throws InterruptedException {
        TextState run = new TextState();
        Thread t1 = new Thread(run);
        Thread t2 = new Thread(run);

        t1.start();
        t2.start();
//        System.out.println(t1.getState());

        while (t1.isAlive() || t2.isAlive()) {
            Thread.sleep(10);
            System.out.println(t1.getName() + " " + t1.getState());
            System.out.println(t2.getName() + " " + t2.getState());
        }



        System.out.println(Thread.currentThread().getState());
/*        for (int i = 0; i < 200; i++) {
            if (i == 100){
                t.join();
                System.out.println(t.getState());
            }
            System.out.println(Thread.currentThread().getName() + "执行到" + i + "步");
        }
    }*/

    }
}
