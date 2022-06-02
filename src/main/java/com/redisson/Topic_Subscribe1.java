package com.redisson;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

public class Topic_Subscribe1 {

    public static final Logger logger = LogManager.getLogger(Topic_Subscribe2.class.getName());

    public static void main(String[] args) throws InterruptedException {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379")
                .setPassword("123456");

        RedissonClient redisson = Redisson.create(config);
        logger.info("Connected");


        RTopic topic3 = redisson.getTopic("Hello");
        topic3.addListener(String.class, (channel, msg) -> {
            logger.info(channel + "; Thread: " + Thread.currentThread().getName());
            logger.info(msg);
        });

        TimeUnit.SECONDS.sleep(20);
        redisson.shutdown();

    }
}
