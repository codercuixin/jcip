package net.jcip.examples.chapter15;

import net.jcip.annotations.ThreadSafe;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/16 19:11
 * 使用CAS实现非阻塞计数器
 */
@ThreadSafe
public class CasCounter {
    private SimulateCAS value;

    public CasCounter(SimulateCAS value) {
        this.value = value;
    }

    public int getValue(){
        return value.get();
    }
    public int increment(){
        int v;
        do{
            v = value.get();
        }while (v!=value.compareAndSwap(v, v+1));
        return v+1;
    }

    public static void main(String[] args) throws InterruptedException {
        CasCounter counter = new CasCounter(new SimulateCAS());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for(int i=0; i< 100000; i++){
                    counter.increment();
                }
            }
        };
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        //主线程等待两个子线程死掉。
        System.out.println(counter.getValue());
    }

}
