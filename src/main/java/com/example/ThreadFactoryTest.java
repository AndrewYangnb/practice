package com.example;

import java.util.concurrent.ThreadFactory;

public class ThreadFactoryTest {

}
class SimpleThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r);
    }
}
