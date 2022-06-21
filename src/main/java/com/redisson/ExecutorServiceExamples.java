package com.redisson;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redisson.Redisson;
import org.redisson.RedissonNode;
import org.redisson.api.RExecutorService;
import org.redisson.api.RFuture;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.annotation.RInject;
import org.redisson.config.Config;
import org.redisson.config.RedissonNodeConfig;

import java.io.Serializable;
import java.util.Collections;
import java.util.concurrent.Callable;

/**
 * 线程池服务
 */

public class ExecutorServiceExamples {

    public static final Logger logger = LogManager.getLogger(ExecutorServiceExamples.class.getName());

    /*RunnableTask 实现 Runnable(可运行任务)类 和 Serializable(序列化)*/
    public static class RunnableTask implements Runnable, Serializable {

        // 指定用Redisson实例填充字段值
        @RInject
        RedissonClient redissonClient;

        // 通过run() 方法内部定义任务逻辑
        @Override
        public void run() {
            RMap<String, String> map = redissonClient.getMap("test-map");
            map.put("3","45");

        }
    }
    public static class CallableTask implements Callable<String>, Serializable {

        @RInject
        RedissonClient redissonClient;

        //通过call() 方法内部定义任务逻辑
        @Override
        public String call() throws Exception {
            RMap<String, String> map = redissonClient.getMap("test-map");
            map.put("1", "2");
            return map.get("3");
        }
    }

    public static void main(String[] args) {
        // 新建redisson客户端配置实例
        Config config = new Config();
        // 给配置文件设置参数，集群模式，添加 节点（Node） 地址
        config.useClusterServers()
                .addNodeAddress("redis://127.0.0.1:7000","redis://127.0.0.1:7001", "redis://127.0.0.1:7002");

        // 创建redissonClient实例
        RedissonClient redissonClient = Redisson.create(config);

        // 新建redisson 的节点配置
        RedissonNodeConfig nodeConfig = new RedissonNodeConfig();
        // 给节点配置文件添加参数，给每个节点设置工作者数量，参数格式要求为映射。  mapping(name, )
        // 通过Collections.singletonMap("myExecutor", 1)，返回一个ImmutableMap，作为参数传入
        nodeConfig.setExecutorServiceWorkers(Collections.singletonMap("myExecutor", 1));
        // 通过nodeConfig创建节点
        RedissonNode node = RedissonNode.create(nodeConfig);
        // 节点启动
        node.start();

        // 建立线程池
        RExecutorService e = redissonClient.getExecutorService("myExecutor");
        // 在将来的某个时间执行给定的任务，
        e.execute(new RunnableTask());
        RFuture<String> future = e.submit(new CallableTask());
        logger.info(future);



        e.shutdown();
        node.shutdown();

    }


}
