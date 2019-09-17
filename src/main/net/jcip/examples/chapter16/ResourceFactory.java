package net.jcip.examples.chapter16;

import net.jcip.annotations.ThreadSafe;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/17 10:43
 * 延迟初始化占位类模式 EJ Item 48
 */
@ThreadSafe
public class ResourceFactory {
    private static class ResourceHolder{
        public static Resource resource = new Resource();
    }

    /**
     * JVM 将推迟ResourceHolder的初始化操作，直到开始使用这个类时才初始化。 [JLS 12.4.1]
     * 并且由于通过一个静态初始化来初始化Resource，因此不需要额外的同步。
     */
    public static Resource getResource(){
        return ResourceHolder.resource;
    }
    static class Resource{

    }
}
