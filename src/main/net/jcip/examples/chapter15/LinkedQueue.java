package net.jcip.examples.chapter15;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/16 20:25
 */
@ThreadSafe
public class LinkedQueue<E> {
    private static class Node<E>{
        final E item;
        final AtomicReference<LinkedQueue.Node<E>> next;
        public Node(E item, LinkedQueue.Node<E> next){
            this.item = item;
            this.next = new AtomicReference<>(next);
        }
    }

    private final Node<E> dummy = new Node<>(null, null );

    private final AtomicReference<Node<E>> head= new AtomicReference<>(dummy);
    private final AtomicReference<Node<E>> tail = new AtomicReference<>(dummy);

    public boolean put(E item){
        Node newNode = new Node(item, null);
        while (true){
            Node<E> curTail = tail.get();
            Node<E> tailNext = curTail.next.get();
            if( curTail == tail.get()){
                if(tailNext!=null){
                    //队列处于中间状态，推进尾节点
                    tail.compareAndSet(curTail, tailNext);
                }else{
                    //处于稳定状态，尝试插入新节点
                    if(curTail.next.compareAndSet(null, newNode)){
                        //插入成功，尝试推进尾节点
                        tail.compareAndSet(curTail, newNode);
                        return true;
                    }
                }
            }
        }
    }
}
