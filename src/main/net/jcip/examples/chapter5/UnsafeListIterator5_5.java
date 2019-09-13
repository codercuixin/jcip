package net.jcip.examples.chapter5;

import java.util.*;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/15 16:00
 */
public class UnsafeListIterator5_5 {
    public static void main(String[] args) {
        //错误演示，Arrays.asList里面没有实现remove等操作。
//        List<String> source = Arrays.asList("Hello", "world");
        List<String> source = new ArrayList<>();
        source.add("Hello");
        source.add("World");
        List<String> list = Collections.synchronizedList(source);
//        wrongForEach(list);
        wrongForEach2(list);
    }

    public static void rightForeach(final List<String> list) {
        synchronized (list) {
            ListIterator<String> listIterator = list.listIterator();
            while (listIterator.hasNext()) {
                String next = listIterator.next();
                System.out.println(next);
            }
        }
    }

    public static void wrongForEach(final List<String> list) {
        ListIterator<String> listIterator = list.listIterator();
        //抛出java.util.ConcurrentModificationException 异常此处，由于返回listIterator用的是底层的数据
        //且底层数据list
        list.remove("Hello");
        while (listIterator.hasNext()) {
            String next = listIterator.next();
            System.out.println(next);
        }
    }

    public static void wrongForEach2(final List<String> list) {

        //抛出java.util.ConcurrentModificationException 异常此处，由于返回listIterator用的是底层的数据
        //且底层数据list
       for (String item: list) {
            System.out.println(item);
            list.remove("Hello");
        }
    }
}
