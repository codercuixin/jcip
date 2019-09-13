package net.jcip.examples.chapter5;

import net.jcip.annotations.GuardBy;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/20 15:42
 * 使用HashMap和synchronized 来实现初始的缓存尝试
 */
public class Memoizer1<A, V> implements Computable<A, V> {
    @GuardBy("this")
    private final Map<A, V> cache = new HashMap<>();
    private final Computable<A, V> c;

    public Memoizer1(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}

class ExpensiveFunction implements Computable<String, BigInteger> {
    @Override
    public BigInteger compute(String arg) throws InterruptedException {
        //after deep thought....
        return new BigInteger(arg);
    }
}