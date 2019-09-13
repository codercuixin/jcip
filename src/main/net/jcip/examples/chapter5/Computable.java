package net.jcip.examples.chapter5;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/20 15:42
 */
public interface Computable<A, V> {
    /**
     * 计算
     * @param arg 参数值
     * @return
     * @throws InterruptedException 中断异常
     */
    V compute (A arg) throws InterruptedException;
}
