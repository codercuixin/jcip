package net.jcip.examples.chapter6;

import java.util.concurrent.Executor;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/2 19:22
 */
public class WithinThreadExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        command.run();
    }
}
