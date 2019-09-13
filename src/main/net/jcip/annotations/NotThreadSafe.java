package net.jcip.annotations;
/*
 * Copyright (c) 2005 Brian Goetz and Tim Peierls
 * Released under the Creative Commons Attribution License
 *   (http://creativecommons.org/licenses/by/2.5)
 * Official home: http://www.jcip.net
 *
 * Any republication or derived work distributed in source code form
 * must include this copyright and license notice.
 */

/**
 * 应用此注解的类不是线程安全的。
 * 这个注解主要用于澄清类的非线程安全性，否则可能被认为是线程安全的，
 * 尽管事实上没有正当理由就假设一个类是线程安全的是一个坏主意。
 * @see ThreadSafe
 */

public @interface NotThreadSafe {
}
