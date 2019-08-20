package net.jcip.examples.chapter5;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.concurrent.CountDownLatch;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/19 16:20
 */
public class TestHarness {
    public Long timeTasks(int nThreads, final Runnable task){
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);
        for(int i =0 ; i<nThreads; i++){
            Thread t = new Thread(){
                @Override
                public void run(){
                    try{
                        startGate.await();
                        try{
                            task.run();
                        }finally {
                            endGate.countDown();
                        }
                    }catch (InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                }
            };
            t.start();
        }
        long start = System.nanoTime();
        startGate.countDown();
        try {
            endGate.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long end = System.nanoTime();
        return end - start;
    }

    public static void main(String[] args){
        TestHarness testHarness = new TestHarness();
        long time =   testHarness.timeTasks(10, new Runnable() {
            @Override
            public void run() {
                System.out.println("task has done");
            }
        });
        System.out.printf("consume %d ns\n", time);

    }


}
