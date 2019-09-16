package net.jcip.examples.chapter14;

import java.util.concurrent.CountDownLatch;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/16 11:28
 */
public class BoundedBuffer<V> extends BaseBoundedBuffer<V> {
    //条件预测： not-full(!isFull())
    //条件预测： not-empty(!isEmpty())
    public BoundedBuffer(int capacity) {
        super(capacity);
    }
    public BoundedBuffer(){
        this(100);
    }

    /**
     * 满足
     * synchronized(lock){
     *     while(!conditionPredicate()){
     *         lock.wait()
     *     }
     * }
     */

    //Blocks-until: not-full
    public synchronized void put(V v) throws InterruptedException {
        while (isFull()){
            //阻塞当前线程，并释放锁。 等待另一个线程调用notify或者notifyAll，然后尝试重新获取锁，然后重新执行。
            this.wait();
        }
        doPut(v);
        this.notifyAll();
    }
    //Blocks-until: not-empty
    public synchronized V take() throws InterruptedException{
        while(isEmpty()){
            this.wait();
        }
        V v = doTake();
        this.notifyAll();
        return v;
    }
    // BLOCKS-UNTIL: not-full
    // Alternate form of put() using conditional notification
    public synchronized void alternatePut(V v) throws InterruptedException {
        while (isFull()){
            this.wait();
        }
        boolean wasEmpty = isEmpty();
        doPut(v);
        if(wasEmpty){
            this.notifyAll();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BoundedBuffer<String> buffer = new BoundedBuffer<>();
        Thread producer = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        buffer.put("hello");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        });
        Thread consumer = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        System.out.println(buffer.take());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        producer.start();
        consumer.start();
    }
}
