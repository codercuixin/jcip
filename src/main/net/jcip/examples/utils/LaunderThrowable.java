package net.jcip.examples.utils;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/19 16:47
 */
public class LaunderThrowable {
    /**
     * 将未经检查的Throwable强制转换为RuntimeException
     * 如果Throwable是一个错误，抛出它;
     * 如果是RuntimeException则返回它，
     * 否则抛出IllegalStateException
     */
    public static RuntimeException launderThrowable(Throwable t){
        if(t instanceof  RuntimeException){
            return (RuntimeException) t;
        }else if( t instanceof Error){
            throw (Error) t;
        }else{
            throw new IllegalArgumentException("Not unchecked",t);
        }
    }
}
