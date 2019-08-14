package net.jcip.examples.chapter4;

import net.jcip.annotations.GuardBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/14 14:17
 * 通过封闭机制来确保线程安全
 */
@ThreadSafe
public class PersonSet {
    @GuardBy("this")
    private final Set<Person> mySet = new HashSet<>();

    public synchronized void addPerson(Person p){
        mySet.add(p);
    }
    public synchronized boolean containsPerson(Person p){
        return mySet.contains(p);
    }

    interface Person{

    }
}
