package net.jcip.examples.chapter08;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/10 20:24
 */
public class ExecutorTest {
    public static void main(String[] args){
        ExecutorService exec = Executors.newCachedThreadPool();
        if(exec instanceof ThreadPoolExecutor){
            ((ThreadPoolExecutor) exec).setCorePoolSize(10);
        }else{
            throw new AssertionError("Oops, bad assumption");
        }

        ExecutorService exec2 = Executors.unconfigurableExecutorService(exec);
        //unconfigurableExecutorService包一层就没有设置属性的方法了
    }
}
