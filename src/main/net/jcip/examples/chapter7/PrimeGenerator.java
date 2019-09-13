package net.jcip.examples.chapter7;

import net.jcip.annotations.GuardBy;
import net.jcip.annotations.ThreadSafe;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/3 10:26
 * 使用一个volatile字段来保存取消状态
 */
@ThreadSafe
public class PrimeGenerator implements Runnable{
    private static ExecutorService exec = Executors.newCachedThreadPool();

    @GuardBy("this")
    private final List<BigInteger> primes = new ArrayList<>();
    private volatile boolean cancelled;

    @Override
    public void run() {
        BigInteger p  = BigInteger.ONE;
        while(!cancelled){
            p = p.nextProbablePrime();
            synchronized (this){
                primes.add(p);
            }
        }
    }

    public void cancel(){
        cancelled = true;
    }
    public synchronized List<BigInteger> get(){
        return new ArrayList<>(primes);
    }

    static List<BigInteger> aSecondOfPrimes() throws InterruptedException {
        PrimeGenerator generator = new PrimeGenerator();
        exec.execute(generator);
        try{
            SECONDS.sleep(1);
        }finally {
            generator.cancel();
        }
        return generator.get();
    }

    public static void main(String args[]) throws InterruptedException {
        for(BigInteger i: aSecondOfPrimes()){
            System.out.println(i.toString());
        }
    }
}
