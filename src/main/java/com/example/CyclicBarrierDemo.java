package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {

    // 定义一个CyclicBarrier实例
    private CyclicBarrier cyclicBarrier;

    // 定义一个二维列表，用来存放所有线程的执行结果，每一个子列表为一个线程的执行结果
    private List<List<Integer>> partialResults
            = Collections.synchronizedList(new ArrayList<>());

    // 使用随机值来模仿线程执行结果
    private Random random = new Random();

    // 定义变量，表示每个线程需要产生的 Integer 数量
    private int NUM_PARTIAL_RESULTS;

    // CyclicBarrier实例中的参与方数量
    private int NUM_WORKERS;

    /**
     * 参与方线程的任务执行逻辑
     */
    class NumberCruncherThread implements Runnable {

        @Override
        public void run() {
            // 获取当前线程的name
            String thisThreadName = Thread.currentThread().getName();
            // 定义一维列表，用来存放该线程的执行结果
            List<Integer> partialResult = new ArrayList<>();

            // Crunch some numbers and store the partial result
            for (int i = 0; i < NUM_PARTIAL_RESULTS; i++) {

                // 生成从0到9的随机数
                Integer num = random.nextInt(10);
                // 打印随机数结果
                System.out.println(thisThreadName
                        + ": Crunching some numbers! Final result - " + num);
                //执行结果存放进一维列表中
                partialResult.add(num);
            }

            // 将 此线程执行完存放结果的一维列表 放到二维列表中（所有线程的执行结果）
            partialResults.add(partialResult);
            try {
                System.out.println(thisThreadName
                        + " waiting for others to reach barrier.");
                // 达到屏障点
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 构造函数中的Runnable实例，最后触发屏障的线程需要额外执行的任务逻辑
     */
    class AggregatorThread implements Runnable {

        @Override
        public void run() {
            // 获取当前线程名称
            String thisThreadName = Thread.currentThread().getName();

            System.out.println(
                    thisThreadName + ": Computing sum of " + NUM_WORKERS
                            + " workers, having " + NUM_PARTIAL_RESULTS + " results each.");
            // 准备存放 所有线程执行结果的 和
            int sum = 0;

            // 取出二维列表中的一维列表
            for (List<Integer> threadResult : partialResults) {
                System.out.print("Adding ");
                // 对一维列表中的每个元素进行求和
                for (Integer partialResult : threadResult) {
                    System.out.print(partialResult+" ");
                    // sum的最终结果是 所有线程执行产生结果 的和
                    sum += partialResult;
                }
                System.out.println();
            }
            System.out.println(thisThreadName + ": Final result = " + sum);
        }
    }


    /**
     * 启动该类的实例
     * @param numWorkers
     * @param numberOfPartialResults
     */
    public void runSimulation(int numWorkers, int numberOfPartialResults) {
        NUM_PARTIAL_RESULTS = numberOfPartialResults;
        NUM_WORKERS = numWorkers;

        // 实例化CyclicBarrier，将触发屏障的线程的 额外任务的执行逻辑作为参数传入构造函数
        cyclicBarrier = new CyclicBarrier(NUM_WORKERS, new AggregatorThread());

        // 提示消息，参与方线程数量 以及 每个线程执行应该产生的结果数
        System.out.println("Spawning " + NUM_WORKERS
                + " worker threads to compute "
                + NUM_PARTIAL_RESULTS + " partial results each");

        // 根据传入的工作者数量，创建线程
        for (int i = 0; i < NUM_WORKERS; i++) {
            // 每个线程的执行逻辑 由 NumberCruncherThread的run() 定义
            Thread worker = new Thread(new NumberCruncherThread());
            // 给没个线程设置名称格式
            worker.setName("Thread " + i);
            worker.start();
        }
    }



    public static void main(String[] args) {
        // 实例化此类
        CyclicBarrierDemo demo = new CyclicBarrierDemo();
        // 调用实例的方法，传入参数，实现线程的执行
        demo.runSimulation(5, 3);
    }


}
