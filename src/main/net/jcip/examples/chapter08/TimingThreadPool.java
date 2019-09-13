package net.jcip.examples.chapter08;

import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/11 9:28
 * 添加日志和计时功能的线程池
 */
public class TimingThreadPool extends ThreadPoolExecutor {
    public TimingThreadPool() {
        super(1, 1, 0L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2));
    }

    private final ThreadLocal<Long> startTime = new ThreadLocal<>();
    private final Logger log = Logger.getLogger("TimingThreadPool");
    private final AtomicLong numTasks = new AtomicLong();
    private final AtomicLong totalTime = new AtomicLong();

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        log.info(String.format("Thread %s: start %s", t, r));
        startTime.set(System.nanoTime());
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        try {
            long endTime = System.nanoTime();
            long taskTime = endTime - startTime.get();
            numTasks.incrementAndGet();
            totalTime.addAndGet(taskTime);
            log.info(String.format("Thread %s: end %s, time=%dna", t, r, taskTime));
        } finally {
            super.afterExecute(r, t);
        }
    }

    @Override
    protected void terminated() {
        try {
            log.info(String.format("Terminated: avg time=%dns", totalTime.get() / numTasks.get()));
            startTime.remove();
        } finally {
            super.terminated();
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        TimingThreadPool timingThreadPool = new TimingThreadPool();
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() {
                System.out.println("Hello world " + new Date());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "end";
            }
        };
        Future<String> f1 = timingThreadPool.submit(callable);
        Future<String> f2 = timingThreadPool.submit(callable);
        f1.get();
        f2.get();
        timingThreadPool.shutdown();
    }
}
