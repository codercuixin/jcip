package net.jcip.examples.chapter12;

/**
 * @author cuixin on 2019-09-15
 * 基于栅栏（barrier）的计时器
 **/
public class BarrierTimer implements Runnable
{
    private boolean started;
    private long startTime, endTime;
    @Override
    public void run() {
        long t = System.nanoTime();
        if(!started){
            started = true;
            startTime = t;
        }else{
            endTime = t;
        }
    }

    public synchronized void clear(){
        started = false;
    }

    public synchronized long getTime(){
        return endTime - startTime;
    }
}
