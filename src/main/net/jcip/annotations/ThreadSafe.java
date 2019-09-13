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
 * 应用此注解的类是线程安全的。
 * 这意味着任何访问序列（对 public 字段的读取和写入，对 public 方法的调用）都不会使对象进入无效状态，
 * 而不管运行时对这些操作的交错，并且不需要调用者的任何额外同步或协调
 */
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ThreadSafe {
}
