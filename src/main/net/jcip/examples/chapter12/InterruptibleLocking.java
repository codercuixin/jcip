package net.jcip.examples.chapter12;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author cuixin on 2019-09-15
 * 可中断的锁获取操作。
 **/

public class InterruptibleLocking {
    private Lock lock = new ReentrantLock();
    private volatile Long threadId;

    public boolean sendOnSharedLine(String message) throws InterruptedException {
        //这个锁获取操作可以中断。
        lock.lockInterruptibly();
        threadId = Thread.currentThread().getId();
        try{
            return cancellableSendOnSharedLine(message);
        }finally {
            lock.unlock();
        }

    }

    public Long getThreadId() {
        return threadId;
    }

    private boolean cancellableSendOnSharedLine(String message) throws InterruptedException {
        /*send message*/
        Thread.sleep(1000);
        System.out.println("threadId--"+Thread.currentThread().getId() + ": send  shared line");
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        InterruptibleLocking instance = new InterruptibleLocking();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    instance.sendOnSharedLine("Hello world");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("threadId--"+ Thread.currentThread().getId() + " has been interrupted ");
                }
            }
        };
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
        //wait for t1, t2 to start
        Thread.sleep(10);
        if(instance.getThreadId().equals(t1.getId())){
            t2.interrupt();
        }else{
            t1.interrupt();
        }


    }


}
