package net.jcip.examples.chapter5;

import net.jcip.annotations.GuardBy;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/15 17:23
 *  隐藏在字符串连接的迭代操作。
 */
public class HiddenIterator {
    @GuardBy("this")
    private final Set<Integer> set = new HashSet<>();

    public synchronized void add(Integer i){
        set.add(i);
    }

    public synchronized void remove(Integer i){
        set.remove(i);
    }

    public void addTenThings(){
        Random r = new Random();
        for(int i=0; i< 10; i++){
            add(r.nextInt());
        }
        //HashSet使用了父类里面的AbstractCollection 的toSting方法， AbstractCollection 的toSting方法会遍历集合。
        //hashCode, equals, containsAll, removeAll, retainAll等(也就是需要遍历列表才能实现的方法）
        // 都可能在遍历过程中抛出ConcurrentModificationException
        System.out.println("DEBUG: added ten elements to " + set);
    }
}
