package net.jcip.examples.chapter10;

import net.jcip.annotations.GuardBy;
import net.jcip.annotations.ThreadSafe;
import net.jcip.examples.chapter4.Point;

import java.util.HashSet;
import java.util.Set;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/12 17:20
 * 通过公开调用（Open Call）来避免在相互协作的对象之间产生死锁
 * 1处，2处，每次都获取一个锁
 */
public class CooperatingNoDeadLock {
    @ThreadSafe
    class Taxi{
        @GuardBy("this")
        private Point location, destination;
        private final Dispatcher dispatcher;

        public Taxi(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        public synchronized Point getLocation() {
            return location;
        }

        /**
         * 1处：先去获取Taxi的锁，释放了再去获取Dispatcher的锁
         * @param location
         */
        public  void setLocation(Point location){
            boolean reachedDestination;
            synchronized (this){
                this.location = location;
                reachedDestination = location.equals(destination);
            }
            if(reachedDestination){
                dispatcher.notifyAvailable(this);
            }
        }

        public synchronized void setDestination(Point destination) {
            this.destination = destination;
        }


    }

    @ThreadSafe
    class Dispatcher{
        @GuardBy("this")
        private final Set<Taxi> taxis;
        private final Set<Taxi> availableTaxis;

        public Dispatcher(){
            this.taxis = new HashSet<>();
            this.availableTaxis = new HashSet<>();
        }
        public synchronized void notifyAvailable(Taxi taxi) {
            availableTaxis.add(taxi);
        }

        /**
         * 2处：获取Dispatcher锁，然后释放再去获取Taxi的锁。
         * @return
         */
        public Image getImage(){
            Set<Taxi> copy = null;
            synchronized (this){
                copy = new HashSet<>(taxis);
            }
            Image image = new Image();
            for(Taxi t: copy){
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
