package net.jcip.examples.chapter08;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/11 10:04
 * 将串行递归转变为并行递归
 */
public class TransformingSequential {

    public static void main(String[] args) throws InterruptedException {
        TransformingSequential instance = new TransformingSequential();
        List<Node<Integer>> list = new ArrayList<>();
        Node<Integer> child1 = new NodeImpl<>(20);
        Node<Integer> child2 = new NodeImpl<>(30);
        list.add(child1);
        list.add(child2);
        Collection<Integer> results = instance.getParallelResults(list);
        for (Integer i : results) {
            System.out.println(i);
        }
    }

    void processSequentially(List<Element> elements) {
        for (Element e : elements) {
            process(e);
        }
    }

    void processInParallel(Executor exec, List<Element> elements) {
        for (final Element e : elements) {
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    process(e);
                }
            });
        }
    }

    <T> void sequentialRecursive(List<Node<T>> nodes, Collection<T> results) {
        for (Node<T> node : nodes) {
            results.add(node.compute());
            sequentialRecursive(node.getChindren(), results);
        }
    }

    public <T> void parallelRecursive(final Executor exec, List<Node<T>> nodes, final Collection<T> results) {
        for (final Node<T> n : nodes) {
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    results.add(n.compute());
                }
            });
            parallelRecursive(exec, n.getChindren(), results);
        }
    }

    public <T> Collection<T> getParallelResults(List<Node<T>> nodes) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        Queue<T> resultQueue = new ConcurrentLinkedQueue<>();
        parallelRecursive(exec, nodes, resultQueue);
        //将所有的已提交的任务执行完
        exec.shutdown();
        //阻塞当前线程，等待所有已提交的任务被执行完。
        exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        return resultQueue;
    }

    void process(Element e) {
        System.out.println(e);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    interface Element {

    }

    interface Node<T> {
        T compute();

        List<Node<T>> getChindren();
    }

    static class ElementImpl implements Element {

    }

    static class NodeImpl<T> implements Node<T> {
        private T val;
        private List<Node<T>> children = new ArrayList<>();

        public NodeImpl(T val) {
            this.val = val;
        }

        public void addChildren(Node<T> node) {
            children.add(node);
        }

        @Override
        public T compute() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return val;
        }

        @Override
        public List<Node<T>> getChindren() {
            return children;
        }
    }
}
