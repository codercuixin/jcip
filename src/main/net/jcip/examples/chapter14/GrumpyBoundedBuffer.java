package net.jcip.examples.chapter14;

import java.util.concurrent.CountDownLatch;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/16 10:16
 */
public class GrumpyBoundedBuffer<V> extends BaseBoundedBuffer<V>{
    public GrumpyBoundedBuffer() {
        super(100);
    }
    public GrumpyBoundedBuffer(int capacity) {
        super(capacity);
    }
    public synchronized void put(V v){
        if(isFull()){
            throw new BufferFullException();
        }
        doPut(v);
    }
    public synchronized V take(){
        if(isEmpty()){
            throw new BufferEmptyException();
        }
        return doTake();
    }
    public static void main(String[] args) throws InterruptedException {
        final GrumpyBoundedBuffer<String> buffer = new GrumpyBoundedBuffer<>();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Thread producer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while(true){
                buffer.put("hello");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                }
            }
        });
        Thread consumer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ExampleUsage exampleUsage = new ExampleUsage(buffer);
                try {
                    exampleUsage.useBuffer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        producer.start();
        consumer.start();
        Thread.sleep(100);
        countDownLatch.countDown();
    }
}
class ExampleUsage{
    private GrumpyBoundedBuffer<String> buffer;

    public ExampleUsage(GrumpyBoundedBuffer<String> buffer) {
        this.buffer = buffer;
    }

    int SLEEP_GRANULARITY = 50;
    void useBuffer() throws InterruptedException {
        while (true){
            try{
                String item = buffer.take();
                System.out.println(item);
//                //use item;
//                break;
            }catch (BufferEmptyException e){
                Thread.sleep(SLEEP_GRANULARITY);
            }
        }
    }
}
class BufferFullException extends RuntimeException {
}

class BufferEmptyException extends RuntimeException {
}