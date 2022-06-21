package com.example;

/**
 * 此类创建的实例用来 提供 生产者线程和消费者线程之间共享数据；
 */
public class Drop {
    // Message sent from producer
    // to consumer.
    private String message;
    // True if consumer should wait
    // for producer to send message,
    // false if producer should wait for
    // consumer to retrieve message.
    // empty用来记录 String message 的状态
    private boolean empty = true;

    /**
     * 该方法用于Consumer拿取message调用，使用synchronized关键字修饰实例方法
     * @return message
     */
    public synchronized String take() {
        // Wait until message is
        // available.
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Toggle status.
        empty = true;
        // Notify producer that
        // status has changed.
        // 唤醒Producer线程
        notifyAll();
        return message;
    }

    /**
     * 该方法用于Producer传送message调用，使用synchronized关键字修饰实例方法
     * @param message Producer要传送的message
     */

    public synchronized void put(String message) {
        // Wait until message has
        // been retrieved.

        // 第一次设置empty=true，该线程不会进入wait状态。
        while (!empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Toggle status.
        // empty状态改变
        empty = false;
        // Store message.
        this.message = message;
        // Notify consumer that status
        // has changed.
        // 唤醒Consumer线程
        notifyAll();
    }
}
