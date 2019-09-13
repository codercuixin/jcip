package net.jcip.examples.chapter4;

import net.jcip.annotations.GuardBy;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/14 15:31
 */
public class PrivateLock {
    private final Object myLock = new Object();
    @GuardBy("myLock")
    Widget widget;

    void someMethod(){
        synchronized (myLock){
            //访问或修改Widget的状态
        }
    }


    interface Widget{

    }
}
