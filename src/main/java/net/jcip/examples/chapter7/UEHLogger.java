package net.jcip.examples.chapter7;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/3 20:42
 */
public class UEHLogger implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Logger logger = Logger.getAnonymousLogger();
        logger.log(Level.SEVERE, "Thread terminated with exception: "+t.getName(), e);
    }
}
