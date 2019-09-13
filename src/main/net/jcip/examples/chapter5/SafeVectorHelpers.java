package net.jcip.examples.chapter5;

import net.jcip.annotations.ThreadSafe;

import java.util.Vector;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/15 15:33
 * 使用客户端锁来实现Vector上的复合操作
 */
@ThreadSafe
public class SafeVectorHelpers {

    public static Object getLast(Vector list){
        //对list加锁，直到多个操作都做完了，才释放锁。
        synchronized (list){
            int lastIndex = list.size() - 1;
            return list.get(lastIndex);
        }
    }

    public static void deleteLast(Vector list){
        synchronized (list){
            int lastIndex = list.size() - 1;
            list.remove(lastIndex);
        }
    }

    public static void foreach(Vector vector){
        //对list加锁，直到多个操作都做完了，才释放锁。
        synchronized (vector) {
            for (int i = 0; i < vector.size(); i++) {
                doSomething(vector.get(i));
            }
        }
    }
    private static  void doSomething(Object object){

    }

}
