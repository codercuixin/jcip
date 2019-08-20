package net.jcip.examples.chapter5;

import net.jcip.annotations.GuardBy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/20 15:48
 * 使用ConcurrentHashMap替代synchronized同步方法。
 *
 */
public class Memoizer2<A, V> implements Computable<A, V> {
    private final Map<A, V> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> c;

    public Memoizer2(Computable<A, V> c) {
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
