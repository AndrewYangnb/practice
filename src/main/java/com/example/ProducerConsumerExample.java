package com.example;

public class ProducerConsumerExample {
    public static void main(String[] args) {
        // 实例化共享数据对象
        Drop drop = new Drop();
        // new生产者线程并启动
        (new Thread(new Producer(drop))).start();
        // new消费者线程并启动
        (new Thread(new Consumer(drop))).start();
    }
}
