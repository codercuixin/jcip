package net.jcip.examples.chapter4;

import java.util.Vector;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/15 13:20
 * 继承Vector并添加put-if-absent方法
 */
public class BetterVector<E> extends Vector<E> {
    /**
     * 当你继承一个可序列化的类时，你应该重新定义serialVersionUID
     */
    static final long serialVersionUID = -3963416950630760754L;

    /**
     * 通过对synchronized对象加锁，从而将判断存在和添加合并为原子操作。
     */
    public synchronized boolean putIfAbsent(E x){
        boolean absent = !contains(x);
        if(absent){
            add(x);
        }
        return absent;
    }

}
