package net.jcip.examples.chapter12;

import junit.framework.TestCase;

/**
 * @author cuixin on 2019-09-14
 * 给BoundedBuffer做基础测试
 **/
public class TestBoundedBuffer extends TestCase {
    private static final long LOCKUP_DETECT_TIMEOUT = 1000;
    private static final int CAPACITY = 10000;
    private static final int THRESHOLD = 10000;

    public void testIsEmptyWhenConstructed(){
        SemaphoreBoundedBuffer<Integer> bb = new SemaphoreBoundedBuffer<>(10);
        assertTrue(bb.isEmpty());
        assertFalse(bb.isFull());
    }
    public void testIsFullAfterPuts() throws InterruptedException {
        SemaphoreBoundedBuffer bb = new SemaphoreBoundedBuffer(10);
        for(int i=0; i<10; i++){
            bb.put(i);
        }
        assertTrue(bb.isFull());
        assertFalse(bb.isEmpty());
    }

    public void testTakeBlockWhenEmpty(){
        final SemaphoreBoundedBuffer<Integer> bb = new SemaphoreBoundedBuffer<>(10);
        Thread taker = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    int unused = bb.take();
                    fail();// if we got here, it's an error
                } catch (InterruptedException e) {
                }
            }
        });
        try{
            taker.start();
            Thread.sleep(LOCKUP_DETECT_TIMEOUT);
            taker.interrupt();
            Thread.sleep(LOCKUP_DETECT_TIMEOUT);
            taker.join(LOCKUP_DETECT_TIMEOUT);
            assertFalse(taker.isAlive());
        } catch (InterruptedException e) {
           fail();
        }
    }
}
