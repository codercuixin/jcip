package net.jcip.examples.chapter3;

import java.util.Collections;
import java.util.HashMap;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/13 17:10
 * 不安全的发布
 */
public class StuffIntoPublic {
    public Holder holder;
    public void initialize(){
        holder = new Holder(42);
    }

    public static void main(String[] args){
        for(int i=0; i< 10; i++) {
            StuffIntoPublic stuffIntoPublic = new StuffIntoPublic();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    stuffIntoPublic.holder.assertSanity();
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    stuffIntoPublic.initialize();
                    stuffIntoPublic.holder.assertSanity();
                }
            }).start();
            Collections.synchronizedMap(new HashMap<>());
        }
    }
}
