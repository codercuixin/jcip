package net.jcip.examples.chapter12;

import net.jcip.annotations.GuardBy;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.Semaphore;

/**
 * @author cuixin on 2019-09-14
 * 使用Semaphore实现Bounded Buffer
 **/
@ThreadSafe
public class SemaphoreBoundedBuffer<E> {
    private final Semaphore availableItems, availableSpaces;
    @GuardBy("this")
    private final E[] items;
    @GuardBy("this")
    private int putPosition = 0, takePosition = 0;

    public SemaphoreBoundedBuffer(int capacity) {
        if (capacity == 0) {
            throw new IllegalArgumentException();
        }
        availableItems = new Semaphore(0);
        availableSpaces = new Semaphore(capacity);
        items = (E[]) new Object[capacity];
    }

    public boolean isEmpty() {
        return availableItems.availablePermits() == 0;
    }

    public boolean isFull() {
        return availableSpaces.availablePermits() == 0;
    }

    public void put(E e) throws InterruptedException {
        availableSpaces.acquire();
        doInsert(e);
        availableItems.release();

    }

    public E take() throws InterruptedException {
        availableItems.acquire();
        E e = doTake();
        availableSpaces.release();
        return e;
    }

    private synchronized void doInsert(E e) {
        int i = putPosition;
        items[i] = e;
        putPosition = (++i == items.length) ? 0 : i;
    }

    private synchronized E doTake() {
        int i = takePosition;
        E e = items[i];
        items[i] = null;
        takePosition = (++i == items.length) ? 0 : i;
        return e;
    }
}
