package net.jcip.examples.chapter7;

import net.jcip.examples.utils.LaunderThrowable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newScheduledThreadPool;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/3 13:53
 * 在一个专门的线程中断任务
 */
public class TimedRun2 {
    private static final ScheduledExecutorService cancelExec = Executors.newScheduledThreadPool(1);
    public static void timedRun(final Runnable r, long timeout, TimeUnit unit) throws InterruptedException {
        class RethrowableTask implements  Runnable{
            private volatile Throwable t;
            @Override
            public void run() {
                try{
                    r.run();
                }catch (Throwable t){
                    this.t = t;
                }
            }
            void rethrow(){
              if(t != null){
                  throw LaunderThrowable.launderThrowable(t);
              }
            }
        }
        RethrowableTask task = new RethrowableTask();
        final Thread taskThread = new Thread(task);
        taskThread.start();
        cancelExec.schedule(new Runnable() {
            @Override
            public void run() {
                taskThread.interrupt();
            }
        }, timeout, unit);
        taskThread.join(unit.toMillis(timeout));
        task.rethrow();
    }


    public static void main(String[] args){

        Runnable r = new Runnable() {
            @Override
            public void run() {
                throw new IllegalArgumentException();
            }
        };
        try {
            timedRun(r, 2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
