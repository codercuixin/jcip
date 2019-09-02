package net.jcip.examples.chapter6;

import net.jcip.examples.utils.LaunderThrowable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/2 20:14
 * Waiting for image download with \Future
 */
public abstract class FutureRender {
    private final ExecutorService executor = Executors.newCachedThreadPool();

    void renderPage(CharSequence source) {
        final List<ImageInfo> imageInfos = scanForImageInfo(source);
        Callable<List<ImageData> > task = new Callable<List<ImageData>>() {
            @Override
            public List<ImageData> call() throws Exception {
               List<ImageData> result = new ArrayList<>();
               for(ImageInfo imageInfo: imageInfos){
                   result.add(imageInfo.downloadImage());
               }
               return result;
            }
        };
        Future<List<ImageData>> future = executor.submit(task);
        renderText(source);
        try {
            List<FutureRender.ImageData> imageData = future.get();
            for (ImageData data : imageData) {
                renderImage(data);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            future.cancel(true);
        } catch (ExecutionException e) {
            throw LaunderThrowable.launderThrowable(e.getCause());
        }
    }

    interface ImageData {
    }

    interface ImageInfo {
        ImageData downloadImage();
    }

    abstract void renderText(CharSequence s);

    abstract List<FutureRender.ImageInfo> scanForImageInfo(CharSequence s);

    abstract void renderImage(FutureRender.ImageData i);

}
