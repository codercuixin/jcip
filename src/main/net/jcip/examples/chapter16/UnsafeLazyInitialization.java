package net.jcip.examples.chapter16;

import net.jcip.annotations.NotThreadSafe;

import java.util.concurrent.CountDownLatch;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/17 10:09
 * 不安全的懒惰初始化
 */
@NotThreadSafe
public class UnsafeLazyInitialization {
    private static Resource resource;
    public static Resource getResource(){
        if(resource == null){
            resource = new Resource();
        }
        return  resource;
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Resource res = getResource();
                System.out.println(res);
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Resource res = getResource();
                System.out.println(res);

            }
        });
        t1.start();
        t2.start();
        Thread.sleep(100);
        countDownLatch.countDown();

    }

    static class Resource{
        int x;
        String str ;
       public Resource(){
           /*比较昂贵的初始化操作*/
           try {
               Thread.sleep(1000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           x = 100;
           str = "hello world";
       }

        @Override
        public String toString() {
            return "Resource{" +
                    "x=" + x +
                    ", str='" + str + '\'' +
                    '}';
        }
    }
}
