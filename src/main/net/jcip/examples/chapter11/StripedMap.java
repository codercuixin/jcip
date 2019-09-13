package net.jcip.examples.chapter11;

import net.jcip.annotations.ThreadSafe;

/**
 * @author cuixin on 2019-09-13
 * 在基于散列的Map上使用使用锁分段技术。
 * 不支持扩容。
 **/
@ThreadSafe
public class StripedMap {
    private static final int N_CLOCKS = 16;
    private final Node[] buckets;
    private final Object[] locks;

    public StripedMap(int numBuckets) {
        buckets = new Node[numBuckets];
        locks = new Object[N_CLOCKS];
        for (int i = 0; i < N_CLOCKS; i++) {
            locks[i] = new Object();
        }
    }

    private final int hash(Object key) {
        return Math.abs(key.hashCode() % buckets.length);
    }

    public Object get(Object key) {
        int hash = hash(key);
        synchronized (locks[hash % N_CLOCKS]) {
            for (Node m = buckets[hash]; m != null; m = m.next) {
                if (m.key.equals(key)) {
                    return m.value;
                }
            }
        }
        return null;
    }

    public void set(Object key, Object value) {
        int hash = hash(key);
        synchronized (locks[hash % N_CLOCKS]) {
            Node newNode = new Node();
            newNode.key = key;
            newNode.value = value;
            Node m = buckets[hash];
            if (m != null) {
                newNode.next = m;
            }
            buckets[hash] = newNode;
        }
    }
    public void print(Object key){
        int hash = hash(key);
        synchronized (locks[hash % N_CLOCKS]) {
            for (Node m = buckets[hash]; m != null; m = m.next) {
                System.out.println(m.value);
            }
        }
    }

    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            synchronized (locks[i % N_CLOCKS]) {
                buckets[i] = null;
            }
        }
    }
    public static void main(String[] args){
        //下面两个hashCode值一致。
        String str1 = "AaAa";
        String str2 = "BBBB";
        StripedMap stripedMap = new StripedMap(48);
        stripedMap.set(str1, "Hello");
        stripedMap.set(str2, "World");
        stripedMap.print(str1);
    }

    private static final class Node {
        Node next;
        Object key;
        Object value;
    }
}
