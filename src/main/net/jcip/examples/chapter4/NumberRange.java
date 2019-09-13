package net.jcip.examples.chapter4;

import net.jcip.annotations.NotThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/14 17:31
 * NumberRange类不足以保护它的不变性条件--即lower永远小于upper
 */
@NotThreadSafe
public class NumberRange {
    //不变性条件：lower <= upper
    private final AtomicInteger lower = new AtomicInteger(0);
    private final AtomicInteger upper = new AtomicInteger(0);

    public void setLower(int i){
        //注意--先检查再操作是不安全的,
        // 这是两个操作，等操作时，可能已经不满足检查时候的条件，所以要加锁，合而为一，变为原子操作。
        if(i> upper.get()){
            throw new IllegalArgumentException("can't set lower to "+ i +" > upper");
        }
        lower.set(i);
    }

    public void setUpper(int i){
        //注意--先检查再操作是不安全的
        if(i< lower.get()){
            throw new IllegalArgumentException("can't set upper to "+ i +" < lower");
        }
        upper.set(i);
    }

    public boolean isInRange(int i){
        return (i >= lower.get() && i<= upper.get());
    }
}
