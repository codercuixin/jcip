package net.jcip.examples.chapter16;

import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/17 11:14
 * 初始化安全性只能确保通过final域可达的值从构造过程完成的可见性。
 * 对于通过非final域可达的值，或者在构造函数后可能改变的值，必须采用同步来确保可见性。
 *
 */
@ThreadSafe
public class SafeStates {
    private final Map<String, String>  states;
    public SafeStates(){
        states = new HashMap<>();
        states.put("alaska","AK");
        states.put("alabama", "AL");
        states.put("wyoming", "wy");
    }
    public String getAbbreviation(String s){
        return states.get(s);
    }
}

/**
 * 问题根源参见Java并发编程的艺术：3.8.2
 *  *  * memory = allocate(); //1:分配对象的内存
 *  *  * ctorInstance(memory); //2: 初始化对象
 *  *  * instance = memory; //3: 设置instance指向刚分配的内存地址
 *  初始化对象与设置instance指向刚分配的内存地址被重排序。
 */
@NotThreadSafe
 class NotSafeStates{
    private  Map<String, String>  states;
    public NotSafeStates(){
        states = new HashMap<>();
        states.put("alaska","AK");
        states.put("alabama", "AL");
        states.put("wyoming", "wy");
    }
    public String getAbbreviation(String s){
        return states.get(s);
    }
}
@NotThreadSafe
class NotSafeStates2{
    private final Map<String, String>  states;
    public NotSafeStates2(){
        states = new HashMap<>();
        states.put("alaska","AK");
        states.put("alabama", "AL");
        states.put("wyoming", "wy");
    }
    public String getAbbreviation(String s){
        return states.get(s);
    }
    //构造过程完成后可能改变states的值，要用同步来保证可见性。
    public Map<String, String> getStates(){
        return states;
    }
}