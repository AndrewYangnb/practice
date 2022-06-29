package com.example;

public class GetState_WAITING {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            Thread t2 = new Thread(new DemoThread2());
            t2.start();
            try {
                t2.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t1.start();
        Thread.sleep(200);
        System.out.println(t1.getState());
    }
}
class DemoThread2 implements Runnable {

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}