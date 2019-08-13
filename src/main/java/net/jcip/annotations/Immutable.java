package net.jcip.annotations;

import java.lang.annotation.*;

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
 * 应用此注解的类是不可变的。 这意味着
 * 调用者无法看到其状态发生变化，这意味着
 * <ul>
 * <li>所有 public 字段都是 final 的，</li>
 * <li>所有 public final 引用字段引用其他不可变对象，</li>
 * <li>构造函数和方法不会发布对实现可能可变的任何内部状态的引用。</li>
 * </ul>
 * 出于性能优化的考虑，不可变对象可能仍然具有内部可变状态;
 * 一些状态变量可以被懒计算，只要它们是从不可变状态计算的，并且调用者无法区分它们。
 <P>
 * 不可变对象本质上是线程安全的; 它们可以在线程之间传递，也可以在没有同步的情况下发布。
 */

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Immutable {
}
