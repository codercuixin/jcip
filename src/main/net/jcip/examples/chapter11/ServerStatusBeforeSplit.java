package net.jcip.examples.chapter11;

import net.jcip.annotations.GuardBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashSet;
import java.util.Set;

/**
 * @author cuixin on 2019-09-13
 * 在锁分解之前
 **/
@ThreadSafe

public class ServerStatusBeforeSplit {
    @GuardBy("this")
    public final Set<String> users;
    @GuardBy("this")
    private final Set<String> queries;

    public ServerStatusBeforeSplit(){
        this.users=new HashSet<>();
        this.queries = new HashSet<>();
    }

    public synchronized void addUser(String user){
        users.add(user);
    }
    public synchronized void addQuery(String query){
        queries.add(query);
    }
    public synchronized void removeUser(String user){
        users.remove(user);
    }
    public synchronized void removeQuery(String query){
        queries.remove(query);
    }
}
