package com.redisson;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

public class LockExample {

    public static final Logger logger = LogManager.getLogger(LockExample.class.getName());

    public static void main(String[] args) throws InterruptedException {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://localhost:6379")
                .setPassword("123456");

        RedissonClient redisson = Redisson.create(config);

        RLock lock = redisson.getLock("lock");
        lock.lock();

        for (int i = 0; i < 2; i++) {
            logger.info("Lock is working>>>>");
            TimeUnit.SECONDS.sleep(1);
        }

        lock.unlock();
        logger.info("Lock is released");

        redisson.shutdown();



    }

}
