package net.jcip.examples.chapter15;

import net.jcip.annotations.ThreadSafe;

import java.sql.PseudoColumnUsage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/16 19:38
 * 基于ReentrantLock实现的伪随机数列
 */
@ThreadSafe
public class ReentrantLockPseudoRandom extends PseudoRandom {
    private final Lock lock = new ReentrantLock(false);
    private int seed;
    public ReentrantLockPseudoRandom(int seed){
        this.seed = seed;
    }
    public int nextInt(int n){
        lock.lock();
        try{
            int s = seed;
            seed = calculateNext(s);
            int remainder = s %n;
            return remainder > 0 ? remainder : remainder + n;
        }finally {
            lock.unlock();
        }
    }
}
