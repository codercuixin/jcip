package net.jcip.examples.chapter14;

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

    //Blocks-until: not-full
    public synchronized void put(V v) throws InterruptedException {
        while (isFull()){
            //阻塞当前线程，并释放锁。 等待另一个线程调用notify或者notifyAll，然后尝试重新获取锁，然后重新执行。
            wait();
        }
        doPut(v);
        notifyAll();
    }
    //Blocks-until: not-empty
    public synchronized V take() throws InterruptedException{
        while(isEmpty()){
            wait();
        }
        V v = doTake();
        notifyAll();
        return v;
    }
    // BLOCKS-UNTIL: not-full
    // Alternate form of put() using conditional notification
    public synchronized void alternatePut(V v) throws InterruptedException {
        while (isFull()){
            wait();
        }
        boolean wasEmpty = isEmpty();
        doPut(v);
        if(wasEmpty){
            notifyAll();
        }
    }
}
