package net.jcip.examples.chapter4;

import net.jcip.annotations.GuardBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/14 15:44
 * 基于监视器模式的车辆追踪
 */
@ThreadSafe
public class MonitorVehicleTracker {
    @GuardBy("this")
    private final Map<String, MutablePoint> locations;

    public MonitorVehicleTracker(Map<String, MutablePoint> locations){
        this.locations = deepCopy(locations);
    }

    public synchronized Map<String, MutablePoint> getLocations(){
        return deepCopy(locations);
    }

    public synchronized MutablePoint getLocation(String id){
        MutablePoint m = locations.get(id);
        return m == null? null: new MutablePoint(m);
    }

    public synchronized void setLocation(String id, int x, int y){
        MutablePoint loc = locations.get(id);
        if(loc == null){
            throw new IllegalArgumentException("No such ID:"+id);
        }
        loc.x = x;
        loc.y = y;
    }


    private static Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> m){
        Map<String, MutablePoint> result = new HashMap<>();
        for(Map.Entry<String, MutablePoint> entry: m.entrySet()){
            result.put(entry.getKey(), new MutablePoint(entry.getValue()));
        }
        return result;
    }
}
