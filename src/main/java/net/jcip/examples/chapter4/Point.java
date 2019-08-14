package net.jcip.examples.chapter4;

import net.jcip.annotations.Immutable;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/14 15:59
 */
@Immutable
public class Point {
    public final int x, y;
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }
}
