package net.jcip.examples.chapter9;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/11 15:29
 * 基于SwingUtilities 实现Executor
 */
public class GuiExecutor extends AbstractExecutorService {
    /**
     * 单例模式，有个私有构造方法和一个公有的工厂方法
     */
    private static final GuiExecutor instance = new GuiExecutor();
    private GuiExecutor(){

    }

    public static GuiExecutor instance(){
        return instance;
    }
    @Override
    public void shutdown() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Runnable> shutdownNow() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void execute(Runnable command) {
        if(SwingUtilities.isEventDispatchThread()){
            command.run();
        }else{
            //扔到swing线程中排队等待
            SwingUtilities.invokeLater(command);
        }
    }
}
