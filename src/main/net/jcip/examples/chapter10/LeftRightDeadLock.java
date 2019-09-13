package net.jcip.examples.chapter10;

import java.util.concurrent.CountDownLatch;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/11 16:44
 * 简单的锁顺序死锁。
 * 查看线程转储Thread Dump信息
 * Unix: kill -3 或者 Ctrl-\
 * Windows: Ctrl-Break
 * IDEA: Debug时的'Get Thread Dump' 摄像头图标
 */
public class LeftRightDeadLock {
    private final Object left = new Object();
    private final Object right = new Object();
    public void leftRight(){
        synchronized (left){
            synchronized (right){
                doSomething();
            }
        }
    }
    public void rightLeft(){
        synchronized (right){
            synchronized (left){
                doSomethingElse();
            }
        }
    }
    void doSomething(){

    }
    void doSomethingElse(){

    }

    public static void main(String[] args){
        LeftRightDeadLock instance = new LeftRightDeadLock();
        //CountDownLatch主要是想循环同时执行
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ///循环次数指定多些，容易复现死锁。 如果你那里没出现死锁，可以继续调大些观察
         int maxCycle = 1000000;
        Thread leftRightThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for(int i=0; i< maxCycle; i++){
                    instance.leftRight();
                }

            }
        });
        Thread rightLeftThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for(int i=0; i< maxCycle; i++){
                    instance.rightLeft();
                }
            }
        });
        leftRightThread.start();
        rightLeftThread.start();
        countDownLatch.countDown();
    }
}
