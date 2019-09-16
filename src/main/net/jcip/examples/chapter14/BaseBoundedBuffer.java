package net.jcip.examples.chapter14;

import net.jcip.annotations.GuardBy;
import net.jcip.annotations.ThreadSafe;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/16 10:10
 */
@ThreadSafe
public class BaseBoundedBuffer<V> {
    @GuardBy("this")
    private final V[] buf;
    @GuardBy("this")
    private int tail;
    @GuardBy("this")
    private int head;
    @GuardBy("this")
    private int count;

    protected BaseBoundedBuffer(int capacity){
        this.buf = (V[]) new Object[capacity];
    }

    protected synchronized final void doPut(V v){
        buf[tail]= v;
        if(++tail == buf.length){
            tail = 0;
        }
        ++count;
    }
    protected synchronized final V doTake(){
        V v = buf[head];
        buf[head] = null;
        if(++head == buf.length){
            head = 0;
        }
        --count;
        return v;
    }
    public synchronized final boolean isFull(){
        return count == buf.length;
    }
    public synchronized final boolean isEmpty(){
        return count == 0;
    }
}
