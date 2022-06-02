package com.redisson;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;


public class Test {

    public static final Logger logger = LogManager.getLogger(Test.class.getName());

    public static void main(String[] args) throws InterruptedException {

        Config config = new Config();
        config.useSingleServer()
                // use "rediss://" for SSL connection
                .setAddress("redis://127.0.0.1:6379")
                .setPassword("123456")
                .setConnectionPoolSize(50)
                .setSubscriptionConnectionPoolSize(50)
                .setTimeout(3000)
                .setRetryAttempts(3)
                .setRetryInterval(1000);

        logger.info("Connecting...");
        RedissonClient redisson = Redisson.create(config);
        logger.info("Connected");

        RBlockingDeque<String> deque = redisson.getBlockingDeque("myQueue");
        deque.add("1");
        deque.add("2");
        deque.add("3");
        deque.add("4");

        logger.info(deque.contains("1"));
        logger.info(deque.peek());
        deque.poll();
        deque.element();

//        for (String string : deque) {
//            deque.remove(string);
//        }

        RSet<String> set = redisson.getSet("mySet");
        set.add("1");
        set.add("2");
        set.add("3");

        logger.info(set.contains("1"));

        RSet<String> secondSet = redisson.getSet("mySecondSet");
        secondSet.add("4");
        secondSet.add("5");

        set.union(secondSet.getName());

        RSetMultimapCache<String, Integer> multimap = redisson.getSetMultimapCache("myMultimap");
        multimap.put("1", 1);
        multimap.put("1", 2);
        multimap.put("1", 3);
        multimap.put("2", 5);
        multimap.put("2", 6);
        multimap.put("4", 7);

        multimap.expireKey("1", 10, TimeUnit.SECONDS);

        redisson.shutdown();

    }

}
