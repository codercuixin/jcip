package net.jcip.examples.chapter14;

import net.jcip.annotations.GuardBy;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/16 15:19
 */
@ThreadSafe
public class SemaphoreOnLock {
    private final Lock lock = new ReentrantLock();
    /**
     * 条件谓词： permitsAvailable(permits > 0)
     */
    private final Condition permitsAvailable = lock.newCondition();
    @GuardBy("lock")
    private int permits;

    SemaphoreOnLock(int initialPermits){
        lock.lock();
        try{
            permits = initialPermits;
        }finally {
            lock.unlock();
        }
    }

    /**
     * 阻塞直到：permitsAvailable
     */
    public void acquire() throws InterruptedException {
        lock.lock();
        try{
            while (permits <= 0){
                permitsAvailable.await();
            }
            --permits;
        }finally {
            lock.unlock();
        }
    }

    public void release(){
        lock.lock();
        try{
            ++permits;
            permitsAvailable.signal();
        }finally {
            lock.unlock();
        }
    }
}
