package net.jcip.examples.chapter11;

import sun.jvm.hotspot.opto.Block;

import java.util.concurrent.BlockingQueue;

/**
 * @author cuixin on 2019-09-13
 * 串行访问任务队列
 **/
public class WorkerThread extends Thread {

    private final BlockingQueue<Runnable> queue;
    public WorkerThread(BlockingQueue<Runnable> quue){
        this.queue = quue;
    }

    @Override
    public void run() {
        while(true){
            try{
                Runnable task = queue.take();
                task.run();
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
