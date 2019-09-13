package net.jcip.examples.chapter6;

import java.util.ArrayList;
import java.util.List;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/2 20:06
 * Rendering page elements sequentially
 */
public abstract class SingleThreadRender {
    void renderPage(CharSequence source) {
        renderText(source);
        List<ImageData> imageData = new ArrayList<ImageData>();
        for (ImageInfo imageInfo : scanForImageInfo(source))
            imageData.add(imageInfo.downloadImage());
        for (ImageData data : imageData)
            renderImage(data);
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
