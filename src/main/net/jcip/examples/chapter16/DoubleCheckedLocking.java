package net.jcip.examples.chapter16;

import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/17 10:55
 * 双重检查加锁，线程不安全；最好用延迟初始化占位模式（静态初始化器及Holder类的按需加锁加载）
 * 问题根源参见Java并发编程的艺术：
 * memory = allocate(); //1:分配对象的内存
 * ctorInstance(memory); //2: 初始化对象
 * instance = memory; //3: 设置instance指向刚分配的内存地址
 * 2，3可能被重排序，那么就是1，3，2，也就是分配对象的内存，设置instance指向刚分配的内存地址，然后在初始化对象
 *
 */
@NotThreadSafe
public class DoubleCheckedLocking {
    private static Resource resource;
    public static Resource getInstance(){
        if(resource == null){
            synchronized (DoubleCheckedLocking.class){
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
