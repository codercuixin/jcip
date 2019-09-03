package net.jcip.examples.chapter7;

import org.omg.PortableServer.THREAD_POLICY_ID;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/3 11:25
 *  使用中断来取消任务
 */
public class PrimeProducer extends Thread {
    private final BlockingQueue<BigInteger> queue;
    PrimeProducer(BlockingQueue<BigInteger> queue){
        this.queue = queue;
    }

    @Override
    public void run() {
       BigInteger p = BigInteger.ONE;
       try{
       while (!Thread.currentThread().isInterrupted()){
           //put 操作，ArrayBlockingQueue.put->ReentrantLock.lockInterruptibly()-> AbstractQueuedSynchronizer->acquireInterruptibly，会调用Thread.interrupted()判断
           //当前线程是否中断，如果中断就抛出InterruptedException中断异常。
           queue.put(p = p.nextProbablePrime());
       }}catch(InterruptedException consumed){
           /*允许线程退出*/
          consumed.printStackTrace();
        }
    }

    public void cancel(){
        this.interrupt();
    }

    static void consumePrimes() throws InterruptedException {
        BlockingQueue<BigInteger> queue = new ArrayBlockingQueue<BigInteger>(5);
        PrimeProducer producer = new PrimeProducer(queue);
        producer.start();
        //等待1000ms,让BrokenPrimeProducer因为空间不足，put操作阻塞
        Thread.sleep(1000);
        try{
            while(needMorePrimes()){
                consume(queue.take());
            }
        }finally {
            //此时可以取消，因为cancel操作中断了线程
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
