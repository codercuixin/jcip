package net.jcip.examples.chapter4;

import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/14 17:13
 */
public class CopyOnWriteArrayListTest {
    public static void main(String[] args){
        List<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        //使用可重入锁来确保线程安全。
        copyOnWriteArrayList.add("Hello");
        copyOnWriteArrayList.add("World");
        //listIterator等遍历器，会在遍历器创建的时候保留底层数组的快照
        //但遍历器创建之后，对于原来的copyOnWriteArrayList的更改的时候，该快照不会再更新。
        ListIterator<String> listIterator =  copyOnWriteArrayList.listIterator();
        copyOnWriteArrayList.add("R U OK");
        System.out.println("---------------listIterator-------------");
        while (listIterator.hasNext()){
            System.out.println(listIterator.next());
        }
        System.out.println();
        //使用可重入锁获得子列表
        List<String> subCopyOnWriteArrayList = copyOnWriteArrayList.subList(0, 3);
        //注意，对子列表的修改会反映到原来的列表中。
        subCopyOnWriteArrayList.add("Thinker");
        System.out.println("---------------CopyOnWriteArrayList-------------");
        copyOnWriteArrayList.forEach(System.out:: println);

    }
}
