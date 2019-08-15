package net.jcip.examples.chapter4;

import net.jcip.annotations.GuardBy;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/15 9:24
 * 通过对this加锁访问实现的线程安全
 */
public class SafePoint {
    @GuardBy("this")
    private int x, y;

    private SafePoint(int[] a) {
        this(a[0], a[1]);
    }

    public SafePoint(int x, int y) {
        set(x, y);
    }

    public SafePoint(SafePoint p) {
        this(p.get());
    }

    public synchronized int[] get() {
        return new int[]{x, y};
    }

    public synchronized void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
