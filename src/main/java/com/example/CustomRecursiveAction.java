package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class CustomRecursiveAction extends RecursiveAction {

    private String workload = "";
    private static final int THRESHOLD = 4;

    private static Logger logger = LogManager.getLogger(CustomRecursiveAction.class.getName());

    // 构造函数，传入要变为大写的字符串
    public CustomRecursiveAction(String workload) {
        this.workload = workload;
    }

    // 覆写compute() 方法，用于定于任务逻辑
    @Override
    protected void compute() {
        // 判断条件，如果长度不满足，切分字符串
        if (workload.length() > THRESHOLD) {
            ForkJoinTask.invokeAll(createSubtasks());
        } else {
            processing(workload);
        }
    }

    // 递归切分任务, 将字符串切成长度符合THRESHOLD 的子字符串
    // 该方法的返回值为 List<CustomRecursiveAction>
    private List<CustomRecursiveAction> createSubtasks() {
        // 子任务为 CustomRecursiveAction
        List<CustomRecursiveAction> subtasks = new ArrayList<>();

        // 将字符串按照长度一分为二
        String partOne = workload.substring(0, workload.length() / 2);
        String partTwo = workload.substring(workload.length() / 2, workload.length());

        // 将拆分好的字符串放到子任务列表中，
        subtasks.add(new CustomRecursiveAction(partOne));
        subtasks.add(new CustomRecursiveAction(partTwo));

        return subtasks;
    }

    // 处理程序，将字符串换为大写
    private void processing(String work) {
        // 将任务中的字符串变为大写
        String result = work.toUpperCase();
        logger.info("This result - (" + result + ") - was processed by "
                + Thread.currentThread().getName());
    }
}
