package net.jcip.examples.chapter14;

import net.jcip.annotations.GuardBy;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/16 14:38
 * 使用显式条件变量的有界缓存
 */
@ThreadSafe
public class ConditionBoundedBuffer<T> {
    protected final Lock lock = new ReentrantLock();
    //条件谓词： notFull(count < items.length)
    private final Condition notFull = lock.newCondition();
    //条件谓词： notEmpty(count>0)
    private final Condition notEmpty = lock.newCondition();

    private static final int BUFFER_SIZE = 100;

    @GuardBy("lock")
    private final T[] items = (T[]) new Object[BUFFER_SIZE];
    @GuardBy("lock")
    private int tail, head, count;

    //阻塞直到：notFull
    public void put(T x) throws InterruptedException {
        //先获取锁
        lock.lock();
        try{
            //循环判断条件谓词
            while(count == items.length){
                //不满足则阻塞当前线程并释放当前的锁
                //直到notFull.signal或者notFull.signalAll被调用，方法await返回之前，会重新获取与condition关联的锁。
                notFull.await();
            }
            items[tail] = x;
            if(++tail == items.length){
                tail = 0;
            }
            ++count;
            notEmpty.signal();
        }finally {
            lock.unlock();
        }
    }
    //阻塞直到：notEmpty
    public T take() throws InterruptedException {
        lock.lock();
        try{
            while (count == 0){
                notEmpty.await();
            }
            T x = items[head];
            items[head] = null;
            if(++head == items.length){
                head = 0;
            }
            --count;
            notFull.signal();
            return x;
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args){
        ConditionBoundedBuffer<String> buffer = new ConditionBoundedBuffer<>();
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
