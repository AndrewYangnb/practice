package com.redisson;

import com.util.RedissonAdapter;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;

import java.util.concurrent.CountDownLatch;

public class RateLimiterExample {
    public static void main(String[] args) throws InterruptedException{
        // RateLimiter——限流，限制对物理资源和逻辑资源的访问速率，保护高并发系统   本地缓存、服务降级
        // QPS——每秒查询率
        RRateLimiter limiter = RedissonAdapter.get("object").getRateLimiter("Test-limiter");
        // 速率模式：OVERALL；速率：1；速率时间间隔：2；速率时间间隔单位：SECOUNDS  每两秒获取一个许可
        limiter.trySetRate(RateType.OVERALL, 1, 2, RateIntervalUnit.SECONDS);

        // 闭锁 所有线程等待一个外部事件
        CountDownLatch latch = new CountDownLatch(1);
        // param：获取的许可证数量
        limiter.acquire(1);

        // 初始化线程t
        Thread t = new Thread(() -> {
            limiter.acquire(1);
            latch.countDown();
        });
        // t线程启动；
        t.start();
        // 等待t线程死掉
        t.join();

        // 阻塞主线程；当latch减为0，countDown释放；
        latch.await();
    }
}
