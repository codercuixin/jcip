package net.jcip.examples.chapter7;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/3 17:24
 * 生产者-消费者 不支持shutdown的日志服务
 */
public class LogWriter {
    private final BlockingQueue<String> queue;
    private final LoggerThread logger;
    private static final int CAPACITY = 1000;

    public LogWriter(Writer writer) {
        this.queue = new LinkedBlockingDeque<>(CAPACITY);
        this.logger = new LoggerThread(writer);
    }

    public void start() {
        logger.start();
    }

    public void log(String msg) throws InterruptedException {
        queue.put(msg);
    }

    private class LoggerThread extends Thread {
        private final PrintWriter writer;

        public LoggerThread(Writer writer) {
            this.writer = new PrintWriter(writer, true);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    writer.println(queue.take());
                }
            } catch (InterruptedException igonred) {

            } finally {
                writer.close();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Writer writer = new PrintWriter(System.out);
        LogWriter logWriter = new LogWriter(writer);
        logWriter.start();
        while(true){
            logWriter.log("Hello");
        }
    }

}
