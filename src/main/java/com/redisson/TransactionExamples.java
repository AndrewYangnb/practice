package com.redisson;

import com.util.RedissonAdapter;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RTransaction;
import org.redisson.api.TransactionOptions;

public class TransactionExamples {
    public static void main(String[] args) {

        RBucket<String> b = RedissonAdapter.get("bucket").getBucket("test");
        b.set("123");

        // 开启事务 隔离级别：已提交读
        // 事务：对写操作使用锁，并维护数据修改操作列表，支持 提交/回滚 操作；
        RTransaction transaction = RedissonAdapter.get("bucket").createTransaction(TransactionOptions.defaults());

        RBucket<String> bucket = transaction.getBucket("test");
        bucket.set("123456");

        RMap<String,String> map = transaction.getMap("Map");
        map.put("1", "a");

        // 提交事务所做的修改
        transaction.commit();

        // 回滚在此事务上所做的所有修改
//        transaction.rollback();

        RedissonAdapter.get("bucket").shutdown();
    }
}
