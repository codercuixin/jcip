package net.jcip.examples.chapter15;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/16 19:28
 * 通过CAS来维持包含多个变了的不可变条件
 */
@ThreadSafe
public class CasNumberRange {
    @Immutable
    private static class IntPair{
        //不变性条件： lower <= upper
        final int lowwer;
        final int upper;

        public IntPair(int lowwer, int upper) {
            this.lowwer = lowwer;
            this.upper = upper;
        }
    }

    private final AtomicReference<IntPair> values = new AtomicReference<>(new IntPair(0,0));

    public int getLower(){
        return values.get().lowwer;
    }
    public int getUpper(){
        return values.get().upper;
    }
    public void setLower(int i){
        while (true){
            IntPair oldV = values.get();
            if(i>oldV.upper){
                throw new IllegalArgumentException("Can't set lower to "+ i+" > upper");
            }
            IntPair newV = new IntPair(i, oldV.upper);
            if(values.compareAndSet(oldV, newV)){
                return;
            }
        }
    }

    public void setUpper(int i){
        while (true){
            IntPair oldV = values.get();
            if(i< oldV.lowwer){
                throw new IllegalArgumentException("Can't set upper to "+i +" < lower");
            }
            IntPair newV = new IntPair(oldV.lowwer, i);
            if(values.compareAndSet(oldV, newV)){
                return;
            }
        }
    }

}
