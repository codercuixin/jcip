package net.jcip.examples.chapter08;

import jdk.nashorn.internal.codegen.CompilerConstants;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/10 17:09
 * 线程饥饿死锁， 在一个单线程的Executor中，任务之间存在依赖性，即父任务等待子线程的结果。
 */
public class ThreadDeadLock {
    static ExecutorService exec = Executors.newSingleThreadExecutor();
    public static class LoadFileTask implements Callable<String>{
        private final String fileName;
        public LoadFileTask(String fileName){
            this.fileName = fileName;
        }
        @Override
        public String call() throws Exception {
            // Here's where we would actually read the file
            return fileName;
        }
    }

    public static class RenderPageTask implements Callable<String>{

        @Override
        public String call() throws Exception {
            Future<String> header,footer;
            header = exec.submit(new LoadFileTask("header.html"));
            footer = exec.submit(new LoadFileTask("footer.html"));
            String page = renderBody();
            //线程饥饿死锁。当前任务等待子任务的执行结果，然而只有一个线程，子任务永远都不会得到执行
            return header.get() + page + footer.get();
        }
        private String renderBody(){
            return "body had rendered";
        }
    }

    public static void main(String[] args){
        exec.submit(new RenderPageTask());
    }
}
