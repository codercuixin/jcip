package net.jcip.examples.chapter12;

import junit.framework.TestCase;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author cuixin on 2019-09-14
 * 给BoundBuffer设计的生产者-消费者
 * CyclicBarrier像极了投票，只有所有参与方(parties)都投票了（await)了，才可以跨过投出结果（跨过障碍），最后一个投票的会鼓掌（执行runCommadn)。
 * 带时间限制的await，超过时间我就不等了，当前线程继续执行。
 **/
public class PutTakeTest extends TestCase {
    protected static final ExecutorService pool = Executors.newCachedThreadPool();
    protected final SemaphoreBoundedBuffer<Integer> bb;
    protected final int nTrais, nPairs;
    protected final AtomicInteger putSum = new AtomicInteger(0);
    protected final AtomicInteger takeSum = new AtomicInteger(0);
    protected CyclicBarrier barrier;

    public PutTakeTest(int capacity, int npairs, int ntrials) {
        this.bb = new SemaphoreBoundedBuffer<>(capacity);
        this.nPairs = npairs;
        this.nTrais = ntrials;
        this.barrier = new CyclicBarrier(2 * capacity + 1);
    }

    public static void main(String[] args) {
        new PutTakeTest(10, 10, 100000).test();
        pool.shutdown();
    }

    static int xorShif(int y) {
        y ^= (y << 6);
        y ^= (y >>> 21);
        y ^= (y << 7);
        return y;
    }

    void test() {
        try {
            for (int i = 0; i < nPairs; i++) {
                pool.execute(new Producer());
                pool.execute(new Consumer());
            }
            barrier.await(); //3处，1个参与方。 1，2，3 处，刚好2N+1, CyclicBarrier可以继续。
            barrier.await(); //6处，1个参与方。 4，5，6 处，刚好2N+1, CyclicBarrier可以继续。
            assertEquals(putSum.get(), takeSum.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    class Producer implements Runnable {
        @Override
        public void run() {
            try {
                int seed = (this.hashCode() ^ (int) System.nanoTime());
                int sum = 0;
                barrier.await(); //1处, N个参与方
                for (int i = nTrais; i > 0; i--) {
                    bb.put(seed);
                    sum += seed;
                    seed = xorShif(seed);
                }
                putSum.getAndAdd(sum);
                barrier.await();//4处, N个参与方

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    class Consumer implements Runnable {
        @Override
        public void run() {
            try {
                barrier.await();//2处, N个参与方
                int sum = 0;
                for (int i = nTrais; i > 0; i--) {
                    sum += bb.take();
                }
                takeSum.getAndAdd(sum);
                barrier.await();//5处, N个参与方

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
