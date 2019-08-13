package net.jcip.examples.chapter3;

import net.jcip.annotations.Immutable;

import java.util.HashSet;
import java.util.Set;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/13 16:29
 */
@Immutable
public class ThreeStooges {
    private final Set<String> stooges = new HashSet<String>();
    public ThreeStooges(){
        stooges.add("Moe");
        stooges.add("Larry");
        stooges.add("Curly");
    }
    public boolean isStooge(String name){
        return stooges.contains(name);
    }
}
