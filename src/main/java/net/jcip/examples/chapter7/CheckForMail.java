package net.jcip.examples.chapter7;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/3 19:21
 *  使用一个私有的executor，并且它的声明周期绑定在方法调用中
 */
public class CheckForMail {
    public boolean checkMail(Set<String> hosts,long timeout, TimeUnit unit) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        final AtomicBoolean hasNewMail = new AtomicBoolean(false);
        try{
            for(final String host: hosts){
                exec.execute(new Runnable() {
                    @Override
                    public void run() {
                        if(checkMail(host)){
                            hasNewMail.set(true);
                        }
                    }
                });
            }
        }finally {
            //shutdown 在ThreadPoolExecutor里面的实现是，更新线程池的runState为SHUTDOWN, 并且给空闲线程设置中断标志。
            exec.shutdown();
            //最多等待指定时间，如果线程池的状态大于SHUTDOWN，则返回true；否则，返回false。
            exec.awaitTermination(timeout, unit);
        }
        return hasNewMail.get();
    }
    private boolean checkMail(String host) {
        // Check for mail
        return false;
    }
}
