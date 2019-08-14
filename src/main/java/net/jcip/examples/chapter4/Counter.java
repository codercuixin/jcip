package net.jcip.examples.chapter4;

import net.jcip.annotations.GuardBy;
import net.jcip.annotations.ThreadSafe;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/14 13:39
 * 使用Java监控器模式的线程安全计数器。
 */
@ThreadSafe
public final class Counter {
    @GuardBy("this")
    private Long value = 0L;

    public synchronized  Long getValue(){
        return value;
    }
    public synchronized  Long increment(){
        if(value == Long.MAX_VALUE){
            throw new IllegalArgumentException("counter overflow");
        }
        return ++value;
    }
}
