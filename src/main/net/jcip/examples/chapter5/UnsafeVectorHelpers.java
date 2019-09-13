package net.jcip.examples.chapter5;

import net.jcip.annotations.NotThreadSafe;

import java.util.Vector;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/15 15:17
 *  在Vector的复合操作可能会导致令人困惑的结果。
 *
 */
@NotThreadSafe
public class UnsafeVectorHelpers {
    public static Object getLast(Vector list){
        //虽然list.size() 和list.get()单独都是对list实例加锁的，
        //但这是两个操作，不能保证原子性。
        //比如getLast和deleteLast相继获的list锁，此时size都为10
        //此时deleteLast删除最后一个；然后getLast获取到size仍为10（脏数据）
        //此时执行list.get操作就会返回ArrayOutOfBoundsException
        int lastIndex = list.size() - 1;
        return list.get(lastIndex);
    }

    public static void deleteLast(Vector list){
        int lastIndex = list.size() - 1;
        list.remove(lastIndex);
    }

    public static void foreach(Vector vector){
        //同getLast，非原子操作。
        for(int i =0 ; i <vector.size(); i++){
            doSomething(vector.get(i));
        }
    }
    private static  void doSomething(Object object){

    }
}
