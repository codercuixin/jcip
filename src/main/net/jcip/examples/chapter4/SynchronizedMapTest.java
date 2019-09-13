package net.jcip.examples.chapter4;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/14 16:40
 */
public class SynchronizedMapTest {
    public static void main(String[] args){
        Map<String, String> map = new HashMap<>();
        //使用装饰者模式，包了一层锁。
        Map<String, String> synchronizedMap =  Collections.synchronizedMap(map);
    }
}
