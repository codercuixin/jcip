package net.jcip.examples.chapter08;

import net.jcip.annotations.GuardBy;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.CountDownLatch;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/11 13:30
 *  由ConcurrentPuzzleSolver使用的携带结果的闭锁。
 */
@ThreadSafe
public class ValueLatch <T>{
    @GuardBy("this")
    private T value = null;

    private final CountDownLatch done = new CountDownLatch(1);

    public boolean isSet(){
        return (done.getCount() == 0);
    }
    public synchronized void setValue(T newValue){
        if(!isSet()){
            value = newValue;
            done.countDown();
        }
    }
    public T getValue() throws InterruptedException {
        done.await();
        synchronized (this){
            return value;
        }
    }
}
