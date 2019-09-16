package net.jcip.examples.chapter14;

import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/16 16:05
 * 使用AbstractQueuedSynchronize实现的二元锁
 */
@ThreadSafe
public class OneShotLatch {
    private final Sync sync = new Sync();
    public void signal(){
        sync.releaseShared(0);
    }
    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(0);
    }

    public static void main(String[] args) throws InterruptedException {
        OneShotLatch latch = new OneShotLatch();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "has continued");
            }
        };
        for(int i= 0; i< 10; i++){
            new Thread(runnable).start();
        }
        Thread.sleep(1000);
        System.out.println("main thread does other things");
        latch.signal();
    }


    private class Sync extends AbstractQueuedSynchronizer{
        /**
         * 要熟悉tryAcquireShared的返回值代表意思
         a negative value on failure; zero if acquisition in shared
         *         mode succeeded but no subsequent shared-mode acquire can
         *         succeed; and a positive value if acquisition in shared
         *         mode succeeded and subsequent shared-mode acquires might
         *         also succeed, in which case a subsequent waiting thread
         *         must check availability. (Support for three different
         *         return values enables this method to be used in contexts
         *         where acquires only sometimes act exclusively.)  Upon
         *         success, this object has been acquired.
         *  返回负数值代表失败；
         *  0代表获取共享锁成功但是接下来的共享模式获取都不会成功
         *  正数值代表获取共享锁成功并且接下来的共享模式获取也可能成功，在这种情况下，下一个等待线程必须检查可用性。
         *  （支持三种返回值是的这个方法可以用在偶尔是独占锁的场景中，todo ReadWriteLock?）
         * @param arg
         * @return
         */
        @Override
        protected int tryAcquireShared(int arg) {
          //如果闭锁是打开的就成功（state ==1）, 否则就失败
            return (getState() == 1) ? 1: -1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            setState(1);
            return true;
        }
    }
}
