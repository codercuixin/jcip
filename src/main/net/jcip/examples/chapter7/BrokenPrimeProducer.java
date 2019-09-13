package net.jcip.examples.chapter7;

import java.math.BigInteger;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/3 10:43
 */
public class BrokenPrimeProducer extends Thread {
    private final BlockingQueue<BigInteger> queue;
    private volatile boolean cancelled = false;
    Object obj = new Object();

    BrokenPrimeProducer(BlockingQueue<BigInteger> queue){
        this.queue = queue;
    }

    @Override
    public void run() {
        try{
            BigInteger p = BigInteger.ONE;
            while (!cancelled){
                queue.put(p = p.nextProbablePrime());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void cancel(){
        cancelled = true;
    }

    static void consumePrimes() throws InterruptedException {
        BlockingQueue<BigInteger> queue = new ArrayBlockingQueue<BigInteger>(5);
        BrokenPrimeProducer producer = new BrokenPrimeProducer(queue);
        producer.start();
        //等待1000ms,让BrokenPrimeProducer因为空间不足，put操作阻塞
        Thread.sleep(1000);
        try{
            while(needMorePrimes()){
                consume(queue.take());
            }
        }finally {
            //此时就不能取消了，因为producer还阻塞在put操作
            producer.cancel();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        consumePrimes();
    }

    static void consume(BigInteger i){
        System.out.println(i);
    }
    static AtomicInteger needNumber = new AtomicInteger(3);
    static boolean needMorePrimes(){
        return needNumber.decrementAndGet() >= 0;
    }


}
