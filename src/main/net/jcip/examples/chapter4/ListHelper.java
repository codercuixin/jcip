package net.jcip.examples.chapter4;

import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/15 13:46
 *  synchronizedList的put-if-absent的线程安全和非线程安全实现示例。
 */
public class ListHelper {

    @NotThreadSafe
    public static class BadListHelper<E>{
        public final List<E> list = Collections.synchronizedList(new ArrayList<>());

        /**
         * 这里对putIfAbsent方法访问加BadListHelper实例锁。
         * 而里面具体的操作加锁的List实例。
         * 所以这里加的BadListHelper实例锁，对于List的两个操作合二为一变为原子操作是没有帮助的。
         */
        public synchronized boolean putIfAbsent(E x){
            boolean absent = !list.contains(x);
            if(absent){
                list.add(x);
            }
            return absent;
        }
    }

    @ThreadSafe
    public static class GoodListHelper<E>{
        public final List<E> list = Collections.synchronizedList(new ArrayList<>());

        /**
         * 由于是想将于List的两个操作合二为一变为原子操作
         * 那么目标很明确--就对List加锁。
         */
        public boolean putIfAbsent(E x){
            synchronized (list){
                boolean absent = !list.contains(x);
                if(absent){
                    list.add(x);
                }
                return absent;
            }
        }
    }
}
