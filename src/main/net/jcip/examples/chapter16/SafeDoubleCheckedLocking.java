package net.jcip.examples.chapter16;

import net.jcip.annotations.ThreadSafe;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/17 11:06
 * 双重检查加锁，线程不安全；最好用延迟初始化占位模式（静态初始化器及Holder类的按需加锁加载）
 *  * 问题根源参见Java并发编程的艺术：3.8.2
 *  * memory = allocate(); //1:分配对象的内存
 *  * ctorInstance(memory); //2: 初始化对象
 *  * instance = memory; //3: 设置instance指向刚分配的内存地址
 */
@ThreadSafe
public class SafeDoubleCheckedLocking {
    /**
     * 通过volatile设置内存屏障，确保不会2，3之间不会重排序。
     */
    private volatile static Resource resource;
    public static Resource getInstance(){
        if(resource == null){
            synchronized (SafeDoubleCheckedLocking.class){
                if(resource == null){
                    resource = new Resource();
                }
            }
        }
        return resource;
    }
    private static class Resource{

    }
}
