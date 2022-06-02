package com.util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * 生成 RedissonClient 实例
 *
 */
public class RedissonAdapter {

    // 新建logger
    public static final Logger logger = LogManager.getLogger();

    // 新建一个Map 用来存放 RedisClient 对象
    private static final Map<String, RedissonClient> redisson = new HashMap<>();

    /**
     * 创建RedisClient
     * @param  name （String）客户端名称
     * @return redisson.get(name)
     */
    public static RedissonClient get(String name) {
        if (redisson.get(name) != null) return redisson.get(name);

        // 保证创建实例时，线程是安全的；
        synchronized (redisson) {
            try {
                // new 一个config对象，用来初始化RedissonClient实例的配置参数
                Config config = new Config();

                logger.info("Connecting Redis[{}]...", name);

                // 给config设置参数
                config.useSingleServer()
                        // redis数据库的url地址
                        .setAddress("redis://127.0.0.1:6379")
                        // redis数据库的密码
                        .setPassword("123456")
                        // 连接池大小
                        .setConnectionPoolSize(64)
                        // 发布和订阅连接池大小
                        .setSubscriptionConnectionPoolSize(10)
                        // 命令等待超时，等待节点回复命令的时间
                        .setTimeout(30000)
                        // 命令失败重复尝试次数
                        .setRetryAttempts(3)
                        // 命令重试发送时间间隔
                        .setRetryInterval(1000);

                // 实例化RedissonClient ，并将其存储到Map中；
                redisson.put(name, Redisson.create(config));
                logger.info("Connected to Redis[{}]", name);

            } catch (Throwable e) {
                // 初始化错误提醒
                logger.error("Redis init error: ", e);
                throw e;
            }
        }

        logger.info(redisson);
        return redisson.get(name);

    }
}
