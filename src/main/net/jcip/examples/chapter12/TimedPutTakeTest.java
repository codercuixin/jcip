package net.jcip.examples.chapter12;

import junit.framework.TestCase;

import java.util.concurrent.CyclicBarrier;

/**
 * @author cuixin on 2019-09-15
 **/
public class TimedPutTakeTest extends PutTakeTest {
    private BarrierTimer timer = new BarrierTimer();

    public TimedPutTakeTest(int cap, int pairs, int trials){
        super(cap, pairs, trials);
        barrier = new CyclicBarrier(pairs * 2 + 1, timer);
    }
    @Override
    public void test(){
        try {
            timer.clear();
            for (int i = 0; i < nPairs; i++) {
                pool.execute(new PutTakeTest.Producer());
                pool.execute(new PutTakeTest.Consumer());
            }
            barrier.await(); //等待达到2N+1的参与方,先执行一次timer，开始producer和consumer,

            barrier.await();//再次等待达到2N+1的参与方,执行一下timer，结束producer和consumer
            long nsPerItem = timer.getTime() / (nPairs * (long)nTrais);
            System.out.print("Throughput:"+nsPerItem+" ns/item");
            assertEquals(putSum.get(), takeSum.get());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args)  {
        //trials per thread 每个线程中的测试线
        int tpt = 100000;
        for(int cap = 1; cap <=1000; cap += 10){
            System.out.println("Capacity: "+cap);
            for(int paris =1; paris <= 128; paris += 2){
                TimedPutTakeTest t = new TimedPutTakeTest(cap, paris,tpt);
                System.out.print("Paris: "+paris+"\t");
                t.test();
                System.out.print("\t");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                t.test();
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
        PutTakeTest.pool.shutdown();

    }
}
