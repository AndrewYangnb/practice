package com.redisson;

import com.util.RedissonAdapter;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RScoredSortedSet;

import java.util.concurrent.TimeUnit;

public class CollectionExamples {

    public static void main(String[] args) throws InterruptedException {
        // 新建一个RList
        RList<String> list = RedissonAdapter.get("collection").getList("Test-list");

        list.add("1");
        list.add("2");
        list.add("3");

        RedissonAdapter.logger.info(list.contains("1"));

        list.addAfter("3", "7");
        list.addBefore("1", "6");

        // works faster than set
        list.fastSet(1, "9");
        list.fastRemove(3);

        // 新建一个RBucket
        RBucket<String> bucket = RedissonAdapter.get("collection").getBucket("Test-Bucket");
        bucket.set("123");
        boolean isUpdated = bucket.compareAndSet("123", "4567");
        String prevObject = bucket.getAndSet("321");
        // tryset: 如果桶为空，则设置值
        boolean isSet = bucket.trySet("904");
        // 返回桶的大小，字节数
        long objectSize = bucket.size();

        bucket.set("value", 10, TimeUnit.SECONDS);
        boolean isNewSet = bucket.trySet("nextValue", 10, TimeUnit.SECONDS);
        RedissonAdapter.logger.info(isUpdated + " " + prevObject + " " + isSet + " " + objectSize + " " + isNewSet);


        RScoredSortedSet<String> set = RedissonAdapter.get("collection").getScoredSortedSet("Test-SortedSet");
        set.add(1, "1");
        set.add(1, "2");
        set.add(4, "3");

        set.addScore("2", 10);
        RedissonAdapter.logger.info(set.readAll());
        RedissonAdapter.get("collection").shutdown();

    }
}
