package net.jcip.examples.chapter12;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author cuixin on 2019-09-14
 * 信号量
 **/
public class SemaphoreUseTest {

    public static void main(String[] args) throws InterruptedException {
//        testAcquireAndRelease();
//        testAcquireCanInterrupt();
        testAcquireCannotInterrupt();

    }

    private static void testAcquireAndRelease(){
        Semaphore semaphore = new Semaphore(1);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + " has get permit");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    semaphore.release();
                }
            }
        };
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        thread1.start();
        thread2.start();



    }

    private static void testAcquireCanInterrupt() throws InterruptedException {
        Semaphore semaphore = new Semaphore(1);

       class Task implements Runnable{
            private final AtomicBoolean hasGotPermit;
            public Task(AtomicBoolean hasGotPermit){
                this.hasGotPermit = hasGotPermit;
            }
            @Override
            public void run() {
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + " has get permit");
                    hasGotPermit.set(true);
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println(Thread.currentThread().getName()+" has been interrupted");
                }finally {
                    semaphore.release();
                }
            }
        };
        AtomicBoolean b1 = new AtomicBoolean(false);
        AtomicBoolean b2 = new AtomicBoolean(false);
        Task task1 = new Task(b1);
        Task task2 = new Task(b2);
        Thread thread1 = new Thread(task1);
        thread1.start();

        Thread thread2 = new Thread(task2);
        thread2.start();
        //主线程稍微sleep一会，等待其中一个获取锁。
        Thread.sleep(10);
        //尝试打断还没有获取permit的acquire操作，可以打断。
        if(b1.get()){
            thread2.interrupt();
        }else if(b2.get()){
            thread1.interrupt();
        }
    }

    private static void testAcquireCannotInterrupt() throws InterruptedException {
        Semaphore semaphore = new Semaphore(1);

        class Task implements Runnable{
            private final AtomicBoolean hasGotPermit;
            public Task(AtomicBoolean hasGotPermit){
                this.hasGotPermit = hasGotPermit;
            }
            @Override
            public void run() {
                try {
                    semaphore.acquireUninterruptibly();
                    System.out.println(Thread.currentThread().getName() + " has get permit");
                    hasGotPermit.set(true);
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println(Thread.currentThread().getName()+" has been interrupted");
                }finally {
                    semaphore.release();
                }
            }
        };
        AtomicBoolean b1 = new AtomicBoolean(false);
        AtomicBoolean b2 = new AtomicBoolean(false);
        Task task1 = new Task(b1);
        Task task2 = new Task(b2);
        Thread thread1 = new Thread(task1);
        thread1.start();

        Thread thread2 = new Thread(task2);
        thread2.start();
        //主线程稍微sleep一会，等待其中一个获取锁。
        Thread.sleep(10);
        //尝试打断还没有获取permit的acquireUninterruptibly操作，发现是不行的。
        if(b1.get()){
            thread2.interrupt();
        }else if(b2.get()){
            thread1.interrupt();
        }
    }





}
