package net.jcip.examples.chapter4;

import net.jcip.annotations.ThreadSafe;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/14 16:03
 * 将线程安全委托给ConcurrentHashMap
 */
@ThreadSafe
public class DelegatingVehicleTracker {
    private final ConcurrentHashMap<String, Point> locations;
    private final Map<String, Point> unmodifiableMap;

    public DelegatingVehicleTracker(Map<String, Point> points){
        locations = new ConcurrentHashMap<>();
        unmodifiableMap = Collections.unmodifiableMap(locations);
    }

    public Map<String, Point> getLocations() {
        return unmodifiableMap;
    }
    public Point getLocation(String id){
        return locations.get(id);
    }
    public void setLocation(String id, int x, int y){
        if(locations.replace(id, new Point(x, y)) == null){
            throw new IllegalArgumentException("invalid vehicle name: "+id);
        }
    }
}
