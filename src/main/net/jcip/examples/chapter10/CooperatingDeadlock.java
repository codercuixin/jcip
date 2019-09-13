package net.jcip.examples.chapter10;

import net.jcip.annotations.GuardBy;
import net.jcip.examples.chapter4.Point;

import java.util.HashSet;
import java.util.Set;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/12 16:50
 * 在相互协作对象之间的锁顺序死锁
 * 1处，2处 获取锁的顺序不一致。
 */

public class CooperatingDeadlock {

    class Taxi{
        @GuardBy("this")
        private Point location, destination;

        private final Dispatcher dispatcher;
        public Taxi(Dispatcher dispatcher){
            this.dispatcher = dispatcher;
        }
        public synchronized Point getLocation(){
            return location;
        }

        /**
         * 1处：先获取Taxi的锁，再获取Dispatcher的锁
         */
        public synchronized void setLocation(Point location) {
            this.location = location;
            if(location.equals(destination)){
                dispatcher.notifyAvailable(this);
            }
        }

        public synchronized void setDestination(Point destination) {
            this.destination = destination;
        }
    }
    class Dispatcher{
        @GuardBy("this")
        private final Set<Taxi> taxis;
        @GuardBy("this")
        private final Set<Taxi> availableTaxis;
        public Dispatcher(){
            taxis = new HashSet<>();
            availableTaxis = new HashSet<>();
        }

        public synchronized void notifyAvailable(Taxi taxi){
            availableTaxis.add(taxi);
        }

        /**
         * 2处：先获取Dispatcher的锁，在获取location的锁。
         * @return
         */
        public synchronized Image getImage(){
            Image image = new Image();
            for(Taxi t: taxis){
                image.drawMarker(t.getLocation());
            }
            return image;
        }
    }
    class Image{
        public void drawMarker(Point p){
            System.out.printf("(%d, %d) ", p.getX(), p.getY());
        }
    }
}
