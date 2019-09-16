package net.jcip.examples.chapter14;

import net.jcip.annotations.GuardBy;
import net.jcip.annotations.ThreadSafe;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/16 14:00
 * 使用wait和notifyAll实现 可重新关闭的阀门
 */
@ThreadSafe
public class ThreadGate {

    //条件谓词：opened-since(n) (isOpen || generation)
    @GuardBy("this")
    private boolean isOpen;
    @GuardBy("this")
    private int generation;

    public synchronized void close(){
        isOpen = false;
    }
    public synchronized void open(){
        ++generation;
        isOpen = true;
        notifyAll();
    }

    //阻塞并直到：opened-since(generation on entry)
    public synchronized void await() throws InterruptedException {
        int arrivalGeneration = generation;
        while(!isOpen && arrivalGeneration == generation){
            wait();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadGate threadGate = new ThreadGate();
        for(int i=0; i<10; i++) {
            Thread waitThread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        threadGate.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName()+" has continued");
                }
            });
            waitThread1.start();
        };
        threadGate.open();

    }


}
