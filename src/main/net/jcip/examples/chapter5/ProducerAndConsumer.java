package net.jcip.examples.chapter5;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/19 10:55
 */
public class ProducerAndConsumer {
        static class FileCrawler implements Runnable{
            private final BlockingQueue<File> fileQueue;
            private final FileFilter fileFilter;
            private final File root;

            public FileCrawler(BlockingQueue<File> fileQueue, final FileFilter fileFilter, File root){
                this.fileQueue = fileQueue;
                this.root =  root;
                this.fileFilter = new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.isDirectory() || fileFilter.accept(f);
                    }
                };
            }


            @Override
            public void run() {
                try{
                    crawl(root);
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            }

            private boolean alreadyIndexed(File f){
                return false;
            }

            private void crawl(File root) throws InterruptedException{
                File[] entries = root.listFiles(fileFilter);
                if(entries != null){
                    for(File entry: entries){
                        if(entry.isDirectory()){
                            crawl(entry);
                        }else if(!alreadyIndexed(entry)){
                            // waiting if necessary for space to become available.
                            fileQueue.put(entry);
                        }
                    }
                }
            }

        }



        static class Indexer implements  Runnable{
            private final BlockingQueue<File> queue;

            public Indexer(BlockingQueue<File> queue){
                this.queue = queue;
            }

            @Override
            public void run() {
                try{
                    while(true){
                        // Retrieves and removes the head of this queue, waiting if necessary
                        // until an element becomes available.
                        indexFile(queue.take());
                    }
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            }

            private void indexFile(File file){
                //index the file
                System.out.println(file.getAbsolutePath());
            }
        }

        private static final int BOUND = 10;
        private static final int N_CONSUMERS = Runtime.getRuntime().availableProcessors();

        public static void startIndexing(File[] roots){
            BlockingQueue<File> queue = new LinkedBlockingDeque<>(BOUND);
            FileFilter fileFilter = new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return true;
                }
            };

            for(File root: roots){
                new Thread(new FileCrawler(queue, fileFilter, root)).start();
            }
            for(int i = 0; i < N_CONSUMERS; i++){
                new Thread(new Indexer(queue)).start();
            }
        }

        public static void main(String[] args){
            File[] roots = new File[]{new File("C:/")};
            startIndexing(roots);
        }

}
