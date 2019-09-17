package net.jcip.examples.chapter15;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/16 20:12
 * 使用Treiber算法构造的非阻塞栈
 */
@ThreadSafe
public class ConcurrentStack <E> {
    AtomicReference<Node<E>> top = new AtomicReference<>();
    public void push(E item){
        Node<E> newHead = new Node<>(item);
        Node<E> oldHead;
        do{
            oldHead = top.get();
            newHead.next = oldHead;
        }while (!top.compareAndSet(oldHead, newHead));
    }

    public E pop(){
        Node<E> newHead;
        Node<E> oldHead;
        do{
            oldHead = top.get();
            if(oldHead == null){
                return null;
            }
            newHead = oldHead.next;
        }while (!top.compareAndSet(oldHead, newHead));
        return oldHead.item;
    }

    private static class Node<E>{
        public final E item;
        public Node<E> next;
        public Node(E item){
            this.item = item;
        }
    }
}
