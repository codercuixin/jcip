package net.jcip.examples.chapter12;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author cuixin on 2019-09-15
 * 尝试在时间限制内加锁
 **/
public class TimedLocking {
    private Lock lock = new ReentrantLock();

    public boolean trySendOnSharedLine(String message, long timeout, TimeUnit unit) throws InterruptedException {
        long nanosToLock = unit.toNanos(timeout)- estimateNanosToSend(message);
        if(!lock.tryLock(timeout, unit)){
            return false;
        }
        try{
            return sendOnSharedLine(message);
        }finally {
            lock.unlock();
        }
    }

    private boolean sendOnSharedLine(String message){
        /* send something*/
        return true;
    }

    private long estimateNanosToSend(String message){
        return message.length();
    }
}
