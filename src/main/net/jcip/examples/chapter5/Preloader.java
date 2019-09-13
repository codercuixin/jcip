package net.jcip.examples.chapter5;

import net.jcip.examples.utils.LaunderThrowable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/19 16:40
 * 使用FutureTask 来提前加载稍后需要使用的数据
 */
public class Preloader {
    ProductInfo loadProductInfo() throws DataLoadException{
        return null;
    }
    private final FutureTask<ProductInfo> future = new FutureTask<>(new Callable<ProductInfo>() {
        @Override
        public ProductInfo call() throws Exception {
            return loadProductInfo();
        }
    });
    private final Thread thread = new Thread(future);

    public void start(){
        thread.start();
    }
    //抛出InterruptedException，让上层去处理
    public ProductInfo get() throws InterruptedException,DataLoadException{
        try {
            return future.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if(cause instanceof DataLoadException){
                throw (DataLoadException) cause;
            }else{
                 throw LaunderThrowable.launderThrowable(e);
            }
        }
    }

    interface ProductInfo{}

}
class DataLoadException extends Exception{

}