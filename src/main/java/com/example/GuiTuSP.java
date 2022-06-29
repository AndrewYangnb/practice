package com.example;

/**
 * 模拟龟兔赛跑
 */
public class GuiTuSP {

    // 用来存储胜利者的ThreadName
    public static String winner;

    static class Race implements Runnable {
        @Override
        public void run() {
            // 记录当前线程的ThreadName
            String threadName = Thread.currentThread().getName();
            // 线程跑步
            for (int i = 0; i <= 100; i++) {

                // 通过作弊，让兔子线程睡觉，每十步睡1ms
                if (threadName.equals("兔子") && i%10 == 0) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                // 判断flag标志，有胜利者，则退出循环，比赛结束
                boolean flag = GuiTuSP.gameOver(i);
                if (flag) {
                    break;
                }
                System.out.println(threadName + "跑了 " + i + "步");
            }
        }
    }
    /**
     * 用来判断是否出现胜利者，返回flag标志
     * @param i - 跑的步数
     * @return boolean
     */
    private static boolean gameOver(int i) {
        if (winner != null) {
            return true;
        } else if (i >= 100){
            winner = Thread.currentThread().getName();
            System.out.println("winner is " + winner);
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Race race = new Race();

        new Thread(race, "兔子").start();
        new Thread(race, "乌龟").start();
    }
}
