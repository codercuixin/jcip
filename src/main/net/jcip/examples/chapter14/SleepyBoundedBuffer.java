package net.jcip.examples.chapter14;

import net.jcip.annotations.ThreadSafe;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/16 11:14
 * 使用简单阻塞实现的有界缓存
 */
@ThreadSafe
public class SleepyBoundedBuffer<V> extends BaseBoundedBuffer<V>{
    int SLEEP_GRANULARITY = 60;
    public SleepyBoundedBuffer(int capacity) {
        super(capacity);
    }
    public SleepyBoundedBuffer(){
        this(100);
    }

    public void put(V v) throws InterruptedException {
        while (true){
            synchronized (this){
                if(!isFull()){
                    doPut(v);
                    return;
                }
            }
            Thread.sleep(SLEEP_GRANULARITY);
        }
    }
    public V take() throws InterruptedException {
        while (true){
            synchronized (this){
                if(!isEmpty()){
                    return doTake();
                }
            }
            Thread.sleep(SLEEP_GRANULARITY);
        }
    }
    public static void main(String[] args){
        SleepyBoundedBuffer<String> buffer = new SleepyBoundedBuffer<>();
        Thread producer = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
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
                while(true) {
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
