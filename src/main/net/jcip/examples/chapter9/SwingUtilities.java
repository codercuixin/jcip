package net.jcip.examples.chapter9;

import java.util.Date;
import java.util.concurrent.*;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/11 15:15
 * 使用Executor实现SwingUtilities
 */
public class SwingUtilities {
    private static final ExecutorService exec = Executors.newSingleThreadExecutor(new SwingThreadFactory());
    private static volatile Thread swingThread;

    private static class SwingThreadFactory implements ThreadFactory{
        @Override
        public Thread newThread(Runnable r) {
            swingThread = new Thread(r);
            return swingThread;
        }
    }
    public static boolean isEventDispatchThread(){
        return Thread.currentThread() == swingThread;
    }
    public static void invokeLater(Runnable task){
        exec.execute(task);
    }

    public static void invokeAndWait(Runnable task){
        Future f =  exec.submit(task);
        try{
            f.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    System.out.println(new Date() + " Task has done");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        SwingUtilities.invokeLater(task);
        System.out.println("continue 1");
        SwingUtilities.invokeAndWait(task);
        //当前线程被SwingUtilities.invokeAndWait中f.get()阻塞
        System.out.println("continue 2");
        System.out.println("isEventDispatchThread=" + isEventDispatchThread());
    }
}
