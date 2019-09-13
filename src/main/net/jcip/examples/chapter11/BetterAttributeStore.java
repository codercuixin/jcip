package net.jcip.examples.chapter11;

import net.jcip.annotations.GuardBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author cuixin on 2019-09-13
 * 减少锁的持有时间
 **/
@ThreadSafe
public class BetterAttributeStore {
    @GuardBy("this")
    private final Map<String, String> attributes = new HashMap<>();

    public  boolean userLocationMatches(String name, String regexp){
        String key = "users."+name+".location";
        String location ;
        synchronized (this){
            location = attributes.get(key);
        }
        if(location == null){
            return false;
        }
        else{
            return Pattern.matches(regexp, location);
        }
    }

}
