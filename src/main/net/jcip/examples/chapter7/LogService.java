package net.jcip.examples.chapter7;

import net.jcip.annotations.GuardBy;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/3 18:21
 * 给LogWriter添加可靠的取消操作
 */
public class LogService {
    private final BlockingQueue<String> queue;
    private final LoggerThread loggerThread;
    private final PrintWriter writer;
    @GuardBy("this")
    private boolean isShutdown;
    @GuardBy("this")
    private int reservations;

    public LogService(Writer writer)
    {
        this.queue = new LinkedBlockingQueue<>();
        this.loggerThread = new LoggerThread();
        this.writer = new PrintWriter(writer);
    }
    public void start(){
        loggerThread.start();
    }

    public void stop(){
        synchronized (this){
            isShutdown = true;
        }
        loggerThread.interrupt();
    }

    public void log(String msg) throws InterruptedException
    {
        synchronized (this){
            if(isShutdown){
                throw new IllegalStateException();
            }
            ++reservations;
        }
        queue.put(msg);
    }

    private class LoggerThread extends Thread{
        @Override
        public void run() {
            try{
                while(true){
                    try{
                        synchronized (LogService.this){
                            if(isShutdown && reservations == 0){
                                break;
                            }
                        }
                        String msg = queue.take();
                        synchronized (LogService.this){
                            --reservations;
                        }
                        writer.println(msg);
                    }catch (InterruptedException ignored){

                    }
                }
            }finally {
                writer.close();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Writer writer = new PrintWriter(System.out);
        LogService logService = new LogService(writer);
        logService.start();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        logService.log("Hello");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        thread.start();
        //2秒之后停止log。
        Thread.sleep(2000);
        logService.stop();


    }

}
