package net.jcip.examples.chapter3;

import net.jcip.annotations.Immutable;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/13 16:38
 */
@Immutable
public class OneValueCache {
    private final BigInteger lastNumber;
    private final BigInteger[] lastFactors;

    public OneValueCache(BigInteger lastNumber, BigInteger[] lastFactors){
        this.lastNumber = lastNumber;
        this.lastFactors = Arrays.copyOf(lastFactors, lastFactors.length);
    }

    public BigInteger[] getFactors(BigInteger i){
        if( lastNumber == null || !lastNumber.equals(i)){
            return  null;
        }else{
            return Arrays.copyOf(lastFactors, lastFactors.length);
        }
    }
}
