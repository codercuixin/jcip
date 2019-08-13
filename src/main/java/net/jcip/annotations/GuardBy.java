/*
 * Copyright (c) 2005 Brian Goetz and Tim Peierls
 * Released under the Creative Commons Attribution License
 *   (http://creativecommons.org/licenses/by/2.5)
 * Official home: http://www.jcip.net
 *
 * Any republication or derived work distributed in source code form
 * must include this copyright and license notice.
 */
package net.jcip.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* 存在这个注解的字段或方法只能在拥有特定锁的时候可以被访问，
 * 这个锁可以是可能是内置（同步）锁，或者是一个显式的java.util.concurrent.Lock。
*
* 参数确定哪个锁保护带注释的字段或方法：
* <ul>
* <li>
* <code> this </code>：定义字段的对象的内部锁定。
* </li>
* <li>
* <code> class-name.this </code>：对于内部类，可能有必要消除'this'的歧义; <em> class-name.this </em>指定允许你指定“this”引用的目标
* </li>
* <li>
* <code>itself</code>：仅供引用字段;字段引用的对象。
* </li>
* <li>
* <code> field-name </code>：锁对象引用自通过<em>field-name</em>指定的（实例或静态）字段。
* </li>
* <li>
* <code> class-name.field-name </code>：锁定对象引用自通过<em> class-name.field-name</em>指定的静态字段。
* </li>
* <li>
* <code> method-name（）</code>：通过调用命名的nil-ary方法返回锁对象。
* </li>
* <li>
* <code> class-name.class </code>：指定类的Class对象应该用作锁对象。
* </li>
*/
@Target({ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface GuardBy {
    String value();
}
