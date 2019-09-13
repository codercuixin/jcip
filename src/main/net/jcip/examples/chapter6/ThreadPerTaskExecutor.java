package net.jcip.examples.chapter6;

import java.util.concurrent.Executor;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/2 19:20
 */
public class ThreadPerTaskExecutor  implements Executor {
    @Override
    public void execute(Runnable command) {
        new Thread(command).start();
    }
}
