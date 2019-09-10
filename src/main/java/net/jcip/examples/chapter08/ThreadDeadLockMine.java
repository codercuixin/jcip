package net.jcip.examples.chapter08;

import java.util.concurrent.*;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/10 17:01
 * 线程饥饿死锁，原因主要是在运行中的线程等待其他资源或条件，导致所有已有的线程阻塞, 也就是说没有多余
 * 的线程去执行在阻塞队列里面的其他任务了。
 * 线程饥饿死锁， 在一个单线程的Executor中，任务之间存在依赖性，即父任务等待子线程的结果。
 */
public class ThreadDeadLockMine {
    static   ExecutorService executorService = Executors.newSingleThreadExecutor();
    public static void main(String[] args){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("execute outer task");
                //只有一个线程，由于外部任务还占有着线程（等待子线程的返回future.get()），没有多余的线程去执行内部的子任务（子任务在阻塞队列中）。
                Future<Integer> future= executorService.submit(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        System.out.println("execute inner task");
                        return 1;
                    }
                });

                //你把下面的future.get()注释掉，就会打印
                //execute outer task
                //execute inner task
                try {
                    //future.get()会阻塞当前线程，等待对应任务完成。
                    //线程饥饿死锁。当前任务等待子任务的执行结果，然而只有一个线程，子任务永远都不会得到执行
                    System.out.println(future.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
