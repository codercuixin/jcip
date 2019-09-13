package net.jcip.examples.chapter4;

import net.jcip.annotations.NotThreadSafe;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/14 15:37
 * 与Java.awt.Point类似的可变Point类
 */
@NotThreadSafe
public class MutablePoint {
    public int x, y;
    public MutablePoint(){
        x = 0;
        y = 0;
    }
    public MutablePoint(MutablePoint p){
        this.x = p.x;
        this.y = p.y;
    }
}
