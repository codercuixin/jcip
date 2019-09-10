package net.jcip.examples.chapter08;

import java.util.concurrent.ThreadFactory;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/10 19:59
 */
public class MyThreadFactory implements ThreadFactory{
    private final String poolName;
    public MyThreadFactory(String poolName){
        this.poolName = poolName;
    }
    @Override
    public Thread newThread(Runnable r) {
       return new MyAppThread(r, poolName);
    }
}
