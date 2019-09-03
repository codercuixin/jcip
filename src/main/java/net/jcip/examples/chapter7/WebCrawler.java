package net.jcip.examples.chapter7;

import net.jcip.annotations.GuardBy;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/3 20:10
 */
public class WebCrawler {
    private volatile TrackingExecutor exec;
    @GuardBy("this")
    private final Set<URL> urlsToCrawl = new HashSet<>();

    private final ConcurrentMap<URL, Boolean> seen = new ConcurrentHashMap<>();
    private static final long TIMEOUT = 500;
    private static final TimeUnit UNIT = TimeUnit.MILLISECONDS;

    public WebCrawler(URL startUrl) {
        urlsToCrawl.add(startUrl);
    }

    public static void main(String[] args) throws MalformedURLException, InterruptedException {
        URL startURL = new URL("http://www.baidu.com/");
        WebCrawler webCrawler = new WebCrawler(startURL);
        webCrawler.start();
        Thread.sleep(5000);
        webCrawler.stop();
        webCrawler.urlsToCrawl.forEach(System.out::println);
    }

    public synchronized void start() {
        exec = new TrackingExecutor(Executors.newCachedThreadPool());
        for (URL url : urlsToCrawl) {
            submitCrawlTask(url);
        }
        urlsToCrawl.clear();
    }

    public synchronized void stop() throws InterruptedException {
        try {
            saveUncrawled(exec.shutdownNow());
            if (exec.awaitTermination(TIMEOUT, UNIT)) {
                saveUncrawled(exec.getTaskCancelledAtShutdown());
            }
        } finally {
            exec = null;
        }
    }

    private void saveUncrawled(List<Runnable> uncrawled) {
        for (Runnable task : uncrawled) {
            urlsToCrawl.add(((CrawlTask) task).getPage());
        }
    }

    private void submitCrawlTask(URL u) {
        exec.execute(new CrawlTask(u));
    }
    static final Random random = new Random();

    private List<URL> processPage(URL url) {
        List<URL> list = new ArrayList<>();
        for(int i= 0; i< 2; i++){
            try {
                URL newUrl = new URL("http://localhost:"+random.nextInt(10000));
                list.add(newUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    private class CrawlTask implements Runnable {
        private final URL url;

        CrawlTask(URL url) {
            this.url = url;
        }

        private int count = 1;

        boolean alreadyCrawled() {
            return seen.putIfAbsent(url, true) != null;
        }

        void markUncrawled() {
            seen.remove(url);
            System.out.printf("marking %s uncrawled%n", url);
        }


        @Override
        public void run() {
            for (URL link : processPage(url)) {
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
                submitCrawlTask(link);
            }
        }

        public URL getPage() {
            return url;
        }
    }
}
