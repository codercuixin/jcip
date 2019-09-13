package net.jcip.examples.chapter7;

import java.util.*;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/3 19:38
 * ExecutorService that keeps track of cancelled tasks after shutdown
 * 主要覆盖了execute方法，getTaskCancelledAtShutdown方法，其他方法都是委托。
 * //todo 有bug。。。。
 */
public class TrackingExecutor extends AbstractExecutorService {
    private final ExecutorService exec;
    //synchronized包了一层
    private final Set<Runnable> taskCancelledAtShutdown = Collections.synchronizedSet(new HashSet<>());

    public TrackingExecutor(ExecutorService exec) {
        this.exec = exec;
    }

    @Override
    public void execute(Runnable command) {
        exec.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    command.run();
                } finally {
                    if (isShutdown() && Thread.currentThread().isInterrupted()) {
                        taskCancelledAtShutdown.add(command);
                    }
                }
            }
        });
    }

    public List<Runnable> getTaskCancelledAtShutdown() {
        if(!exec.isTerminated()){
            throw new IllegalArgumentException();
        }
        return new ArrayList<>(taskCancelledAtShutdown);
    }

    @Override
    public void shutdown() {
        exec.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return exec.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return exec.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return exec.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return exec.awaitTermination(timeout, unit);
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        TrackingExecutor trackingExecutor = new TrackingExecutor(executorService);

        for (int i = 0; i < 100; i++) {
            trackingExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    while (!Thread.currentThread().isInterrupted()) {

                    }
                }
            });
        }


        List<Runnable> list =  trackingExecutor.shutdownNow();
        if (list.size() > 0) {
            list.forEach(System.out::println);
        }
    }
}
