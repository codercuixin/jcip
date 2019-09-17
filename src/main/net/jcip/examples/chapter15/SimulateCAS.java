package net.jcip.examples.chapter15;

import net.jcip.annotations.GuardBy;
import net.jcip.annotations.ThreadSafe;
import sun.misc.Unsafe;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/16 19:04
 * 模拟CAS操作
 */
@ThreadSafe
public class SimulateCAS {
    @GuardBy("this")
    private int value;

    public synchronized int get(){
        return value;
    }
    public synchronized int compareAndSwap(int expectedValue, int newValue){
        int oldValue = value;
        if(oldValue == expectedValue){
            value = newValue;
        }
        return oldValue;
    }
    public synchronized boolean compareAndSet(int expectedValue, int newValue){
        return (expectedValue == compareAndSwap(expectedValue, newValue));
    }
}
