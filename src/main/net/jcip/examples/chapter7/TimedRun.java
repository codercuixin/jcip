package net.jcip.examples.chapter7;

import net.jcip.examples.utils.LaunderThrowable;

import java.util.concurrent.*;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/3 14:22
 * 使用Future来取消一个任务
 */
public class TimedRun {
    private static final ExecutorService taskExec = Executors.newCachedThreadPool();

    public static void timedRun(Runnable r, long timeout, TimeUnit unit) throws InterruptedException {
        Future<?> task = taskExec.submit(r);
        try {
            task.get(timeout, unit);
        } catch (InterruptedException e) {
            throw e;
        } catch (ExecutionException e) {
            throw LaunderThrowable.launderThrowable(e);
        } catch (TimeoutException e) {
            //任务将被取消
            e.printStackTrace();
        } finally {
            //如果任务已经完成也是无害的
            //如果线程正在运行，也是可以打断的
            //futureTask底层还是调用Thread.interrupt 加上CAS设置字段state。
            task.cancel(true);
        }
    }

    public static void main(String[] args) {

        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (true) {

                }
            }
        };
        try {
            timedRun(r, 2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
