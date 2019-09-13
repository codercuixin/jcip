package net.jcip.examples.chapter11;

import net.jcip.annotations.GuardBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author cuixin on 2019-09-13
 * 将一个锁不必要地持有过长时间。
 **/
@ThreadSafe
public class AttributeStore {
    @GuardBy("this")
    private final Map<String, String> attributes = new HashMap<>();

    public synchronized boolean userLocationMatches(String name, String regexp){
        String key = "users."+name+".location";
        String location = attributes.get(key);
        if(location == null){
            return false;
        }
        else{
            return Pattern.matches(regexp, location);
        }
    }

}
