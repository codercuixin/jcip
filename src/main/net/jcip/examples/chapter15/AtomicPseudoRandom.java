package net.jcip.examples.chapter15;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/16 19:41、
 * 基于AtomicInteger实现的伪随机数列
 */
@ThreadSafe
public class AtomicPseudoRandom extends PseudoRandom {
    private AtomicInteger seed;
    public AtomicPseudoRandom(int seed){
        this.seed = new AtomicInteger(seed);
    }
    public int nextInt(int n){
        while (true){
            int s = seed.get();
            int nextSeed = calculateNext(s);
            if(seed.compareAndSet(s, nextSeed)){
                int remainder = s %n;
                return remainder > 0? remainder: remainder+n;
            }
        }
    }
}
