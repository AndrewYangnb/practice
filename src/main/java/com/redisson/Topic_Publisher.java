package com.redisson;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class Topic_Publisher {

    public static final Logger logger = LogManager.getLogger(Topic_Publisher.class.getName());

    public static void main(String[] args) throws InterruptedException {

        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://localhost:6379")
                .setPassword("123456");
        RedissonClient redisson = Redisson.create(config);

        RTopic topic2 = redisson.getTopic("Hello");
        logger.info("Publisher is working ......");
        topic2.publish("wo");
        topic2.publish("ai");
        topic2.publish("ni");
        topic2.publish("men");
        topic2.publish("!!!");
        logger.info("Publisher's work is done.");

        redisson.shutdown();
    }
}
