package net.jcip.examples.chapter16;

import net.jcip.annotations.ThreadSafe;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/17 10:38
 * 提前初始化
 */
@ThreadSafe
public class EagerInitialization {
    /**
     * 静态初始化器由JVM在类的初始化阶段执行；而JVM在类的初始化阶段会获取一个锁 [JLS 12.4.2]，并且每个
     * 都至少获取一次这个锁以确保这个类已经加载，因此在静态初始化期间，内存写入操作对所有线程可见。
     */
    private static Resource resource = new Resource();
    private static class Resource{

    }
}
