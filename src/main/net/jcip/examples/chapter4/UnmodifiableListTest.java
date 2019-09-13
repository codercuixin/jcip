package net.jcip.examples.chapter4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/14 16:34
 *  Collections.unmodifiableXXX(Arg)的返回结果R，不可以修改，这是通过覆盖修改方法直接抛出UnsupportedException实现的。
 *  但是如果是修改的是Arg，那么会反映到返回结果R中，因为R 底层的数据引用的是Arg。
 */
public class UnmodifiableListTest {
    public static void main(String args[]){
        List<String> sources = new ArrayList<>();
        sources.add("R U OK");
        sources.add("I L U");
        sources.add("I M U");
        sources.add("I T U");

        List<String> unmodifiableList =  Collections.unmodifiableList(sources);
        try{
            unmodifiableList.add("Test");
        }catch (Exception e){
            System.err.println(e);
        }
        sources.add("I am new here");
        unmodifiableList.forEach(System.out::println);
    }
}
