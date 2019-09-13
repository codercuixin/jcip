package net.jcip.examples.chapter6;

import java.util.List;
import java.util.concurrent.*;

import static net.jcip.examples.utils.LaunderThrowable.launderThrowable;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/2 20:26
 *  ExecutorCompletionService 底层封装了一下基于 BlockingQueue, Executor，代码不复杂
 *  把Future放在BlockingQueue
 */
public abstract class Render {
    private final ExecutorService executor;

    Render(ExecutorService executor){
        this.executor = executor;
    }
    void renderPage(CharSequence source){
        final List<ImageInfo> info = scanForImageInfo(source);
        CompletionService<ImageData> completionService = new ExecutorCompletionService<>(executor);
        for(final ImageInfo imageInfo: info){
            completionService.submit(new Callable<ImageData>() {
                @Override
                public ImageData call() throws Exception {
                    return imageInfo.downloadImage();
                }
            });
        }
        renderText(source);
        try{
        for(int t=0, n = info.size(); t < n; t++){
            Future<ImageData> f = completionService.take();
            ImageData imageData = f.get();
            renderImage(imageData);
        }} catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw launderThrowable(e.getCause());
        }

    }

    interface ImageData {
    }

    interface ImageInfo {
        ImageData downloadImage();
    }

    abstract void renderText(CharSequence s);

    abstract List<ImageInfo> scanForImageInfo(CharSequence s);

    abstract void renderImage(ImageData i);
}
