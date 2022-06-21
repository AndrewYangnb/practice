package com.example;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinPoolExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // RecursiveAction任务，没有返回值
        CustomRecursiveAction customRecursiveAction = new CustomRecursiveAction("abcdefghijklmnopqrst");

        // RecursiveTask任务，有返回值
        int[] arr = new int[] {10, 20, 30, 24, 15};
        CustomRecursiveTask customRecursiveTask = new CustomRecursiveTask(arr);

        // 创建一个ForkJoinPool线程池
        ForkJoinPool forkJoinPool = new ForkJoinPool(3);

        // 将任务提交给线程池，并执行任务
        forkJoinPool.invoke(customRecursiveAction);
        int result = forkJoinPool.invoke(customRecursiveTask);
        System.out.println(result);

    }
}
