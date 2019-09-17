package net.jcip.examples.chapter15;

import java.util.concurrent.*;

public class PseudoRandom {
    int calculateNext(int prev) {
        prev ^= prev << 6;
        prev ^= prev >>> 21;
        prev ^= (prev << 7);
        return prev;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ReentrantLockPseudoRandom reentrantLockPseudoRandom = new ReentrantLockPseudoRandom(1000);
        AtomicPseudoRandom atomicPseudoRandom = new AtomicPseudoRandom(1000);

        Callable<Long> r1 = new Callable() {
            @Override
            public Long call() {
                long startTime = System.currentTimeMillis();
                long sum = 0;
                long count = 0;
                while (System.currentTimeMillis() - startTime <= 5000) {
//                    int res=  atomicPseudoRandom.nextInt(100);
                    int res=  reentrantLockPseudoRandom.nextInt(100);
                    count += 1;
                    sum += res;
                }
                System.out.println(sum);
                return count;
            }
        };
        ExecutorService exec = Executors.newFixedThreadPool(4);
        Future<Long> f1 = exec.submit(r1);
        Future<Long> f2 = exec.submit(r1);
        Future<Long> f3 = exec.submit(r1);
        Future<Long> f4 = exec.submit(r1);
        System.out.println(f1.get() + f2.get() + f3.get() + f4.get() );
    }
}