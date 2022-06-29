package com.example;

import java.util.concurrent.*;

public class SynchronousTest {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        SynchronousQueue<Integer> queue = new SynchronousQueue<>();

        Runnable producer = () -> {
            Integer producedElement = ThreadLocalRandom
                    .current()
                    .nextInt();
            Integer producedElement1 = ThreadLocalRandom
                    .current()
                    .nextInt();
            try {
                queue.put(producedElement);
                System.out.println("producer生产了" + producedElement);
                queue.put(producedElement1);
                System.out.println("producer生产了" + producedElement1);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        };

        Runnable consumer = () -> {
            try {
                Integer consumedElement = queue.take();
                System.out.println("Consumer拿到了" + consumedElement);
                Integer consumedElement1 = queue.take();
                System.out.println("Consumer拿到了" + consumedElement1);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        };

        executor.execute(producer);
        executor.execute(consumer);

        executor.awaitTermination(500, TimeUnit.MILLISECONDS);
        executor.shutdown();

    }

}
