package com.example;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PriorityBlockingQ {

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 0, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue<>(), Thread::new, new ThreadPoolExecutor.AbortPolicy());
        executor.execute(new Task(100));
        executor.execute(new Task(5));
        executor.execute(new Task(3));
        executor.execute(new Task(4));

        executor.shutdown();
    }

}


class Task implements Runnable, Comparable<Task> {

    private final int priority;

    public int getPriority() {
        return this.priority;
    }

    public Task(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(Task anotherTask) {
        return anotherTask.getPriority() - getPriority();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " collect data successfully. the priority of this task is : " + priority);
    }
}