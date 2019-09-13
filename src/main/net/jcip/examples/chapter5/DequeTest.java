package net.jcip.examples.chapter5;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/19 15:25
 * Double ended queue 双端队列测试
 */
public class DequeTest {
    public static void main(String[] args) {
        Deque<String> deque = new LinkedBlockingDeque<>();
        deque.push("Hello");
        deque.push("World");
        deque.push("My name is YellowStar5");
        System.out.println(deque.getFirst());
        System.out.println(deque.getLast());

        System.out.println(deque.peekFirst());
        System.out.println(deque.peekLast());
    }
}
