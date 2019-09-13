package net.jcip.examples.chapter11;

import net.jcip.annotations.GuardBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashSet;
import java.util.Set;

/**
 * @author cuixin on 2019-09-13
 * 使用锁分解对ServerStatus重构
 **/
@ThreadSafe
public class ServerStatusAfterSplit {
    @GuardBy("users")
    public final Set<String> users;
    @GuardBy("queries")
    private final Set<String> queries;

    public ServerStatusAfterSplit(){
        this.users=new HashSet<>();
        this.queries = new HashSet<>();
    }

    public void addUser(String user){
        synchronized (users){
            users.add(user);
        }
    }
    public synchronized void addQuery(String query){
        synchronized (queries){
            queries.add(query);
        }
    }
    public synchronized void removeUser(String user){
        synchronized (users){
            users.remove(user);
        }
    }
    public synchronized void removeQuery(String query){
        synchronized (queries){
            queries.remove(query);
        }
    }
}
