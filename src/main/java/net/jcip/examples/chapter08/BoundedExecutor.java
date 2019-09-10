package net.jcip.examples.chapter08;

import net.jcip.annotations.ThreadSafe;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/10 18:54
 * 使用信号量来限制任务提交
 */
@ThreadSafe
public class BoundedExecutor {
    private final Executor exec;
    private final Semaphore semaphore;

    public BoundedExecutor(Executor exec, Semaphore semaphore) {
        this.exec = exec;
        this.semaphore = semaphore;
    }

    public void submitTask(final Runnable command) throws InterruptedException {
        semaphore.acquire();
        try{
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        command.run();
                    }finally {
                        semaphore.release();
                    }
                }
            });
        }catch (RejectedExecutionException e){
            semaphore.release();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Executor executor = Executors.newCachedThreadPool();
        Semaphore semaphore = new Semaphore(2);
        BoundedExecutor boundedExecutor = new BoundedExecutor(executor, semaphore);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(new Date()+ " hello");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        boundedExecutor.submitTask(runnable);
        boundedExecutor.submitTask(runnable);
        boundedExecutor.submitTask(runnable);
    }
}
