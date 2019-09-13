package net.jcip.examples.chapter6;

import java.util.concurrent.*;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/2 20:55
 * 超时后就取消任务，返回默认广告
 */
public class RenderWithTimeBudget {
    private static final Ad DEFAULT_AD = new Ad();
    private static final long TIME_BUDGET = 1000;
    private static final ExecutorService exec = Executors.newCachedThreadPool();

    Page renderPageWithAd(){
        Long endNanos = System.nanoTime() + TIME_BUDGET;
        Future<Ad> f = exec.submit(new FetchAdTask());
        Page page = renderPageBody();
        Ad ad;
        try{
         long timeLeft = endNanos -System.nanoTime();
         ad = f.get(timeLeft, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            ad =DEFAULT_AD;
        } catch (ExecutionException e) {
            ad =DEFAULT_AD;
        } catch (TimeoutException e) {
            ad = DEFAULT_AD;
            f.cancel(true);
        }
        page.setAd(ad);
        return page;
    }


    static class Ad {
    }
    Page renderPageBody() { return new Page(); }


    static class Page {
        public void setAd(Ad ad) { }
    }
    static class FetchAdTask implements Callable<Ad>{
        @Override
        public Ad call() throws Exception {
            return new Ad();
        }
    }
}
