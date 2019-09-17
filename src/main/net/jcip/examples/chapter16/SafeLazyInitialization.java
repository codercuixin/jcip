package net.jcip.examples.chapter16;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/17 10:28
 * 线程安全的懒惰初始化
 */
public class SafeLazyInitialization {
    private static Resource resource;
    public synchronized static Resource getInstance(){
        if(resource == null){
            resource = new Resource();
        }
        return resource;
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
