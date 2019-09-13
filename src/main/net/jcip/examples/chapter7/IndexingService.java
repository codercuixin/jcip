package net.jcip.examples.chapter7;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/3 18:51
 * 通过“毒丸”来关闭
 * 生产者生产毒丸后就不再生产。
 * 消费消费毒丸后就不再消费。
 */
public class IndexingService {
    private static final int CAPACITY = 1000;
    private static final File POISON = new File("");
    private final IndexThread consumer = new IndexThread();
    private final CrawlThread producer = new CrawlThread();
    private final BlockingQueue<File> queue;
    private final FileFilter fileFilter;
    private final File root;

    public IndexingService(File root, final FileFilter fileFilter) {
        this.root = root;
        this.queue = new LinkedBlockingQueue<>(CAPACITY);
        this.fileFilter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || fileFilter.accept(f);
            }
        };
    }
    public void start(){
        producer.start();
        consumer.start();
    }
    public void stop()
    {
        producer.interrupt();
    }

    public void awaitTermination() throws InterruptedException {
        consumer.join();
    }

    private boolean alreadyIndexed(File f) {
        return false;
    }


    class CrawlThread extends Thread {
        @Override
        public void run() {
            try {
                crawl(root);
            } catch (InterruptedException ignored) {
            } finally {
                while (true) {
                    try {
                        queue.put(POISON);
                        System.out.println("produce poison");
                        break;
                    } catch (InterruptedException ignored) {

                    }
                }
            }
        }

        private void crawl(File root) throws InterruptedException {
            File[] entries = root.listFiles(fileFilter);
            if (entries != null) {
                for (File entry : entries) {
                    if (entry.isDirectory()) {
                        crawl(entry);
                    } else if (!alreadyIndexed(entry)) {
                        queue.put(entry);
                    }
                }
            }
        }
    }

    class IndexThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    File file = queue.take();
                    if (file == POISON) {
                        System.out.println("consume poison");
                        break;
                    } else {
                        indexFile(file);
                    }
                }
            } catch (InterruptedException ignored) {

            }
        }

        public void indexFile(File file) {
            System.out.println(file.getName());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final  FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.length() >= 2;
            }
        };
        IndexingService indexingService = new IndexingService(new File("C:/"), fileFilter);
        indexingService.start();
        Thread.sleep(100);
        indexingService.stop();
    }
}
