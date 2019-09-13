package net.jcip.examples.chapter4;

import net.jcip.annotations.ThreadSafe;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/15 11:21
 *  安全发布SafePoint的车辆追踪器。
 */
@ThreadSafe
public class PublishingVehicleTracker {
    private final Map<String, SafePoint> locations;
    private final Map<String, SafePoint> unmodifiableMap;

    public PublishingVehicleTracker(Map<String, SafePoint> locations){
        this.locations = new ConcurrentHashMap<>(locations);
        this.unmodifiableMap = Collections.unmodifiableMap(this.locations);
    }

    public Map<String, SafePoint> getLocations(){
        return unmodifiableMap;
    }

    public SafePoint getLocation(String id){
        return locations.get(id);
    }
    public void setLocation(String id, int x, int y){
        if(!locations.containsKey(id)){
            throw new IllegalArgumentException("invalid vehicle name: "+id);
        }
        locations.get(id).set(x, y);
    }

}
