package net.jcip.examples.chapter3;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/13 17:06
 * 如果没有正确发布，则有失败风险的类
 */
public class Holder {
    private int n;
    public Holder(int n){
        this.n = n;
    }
    public void assertSanity(){
        if( n != n){
            throw new AssertionError("This statement is false");
        }
    }
}
