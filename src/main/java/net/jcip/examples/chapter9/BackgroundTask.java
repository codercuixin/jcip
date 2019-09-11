package net.jcip.examples.chapter9;

import java.util.concurrent.*;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/11 15:51
 * 支持取消， 完成通知以及进度标识的后台任务类
 */
public abstract class BackgroundTask<V> implements Runnable, Future<V> {
    private final FutureTask<V> computation = new Computation();
    private class Computation extends FutureTask<V>{
        public Computation(){
            super(new Callable<V>() {
                @Override
                public V call() throws Exception {
                    return BackgroundTask.this.compute();
                }
            });
        }

        @Override
        protected void done() {
            GuiExecutor.instance().execute(new Runnable() {
                @Override
                public void run() {
                    V value = null;
                    Throwable thrown = null;
                    boolean cancelled = false;
                    try{
                        value = get();
                    } catch (InterruptedException consumed) {

                    } catch (ExecutionException e) {
                        thrown  = e.getCause();
                    }catch(CancellationException e){
                        cancelled = true;
                    }finally {
                        onCompletion(value, thrown, cancelled);
                    }
                }
            });
        }
    }


    /**
     * called in the background thread
     */
    protected abstract V compute()throws Exception;

    //called in the event thread
    protected void onCompletion(V result, Throwable throwable, boolean canceled){

    }
    protected void onProgress(int current, int max){

    }

    protected void setProgress(final int current, final int max){
        GuiExecutor.instance().execute(new Runnable() {
            @Override
            public void run() {
                onProgress(current, max);
            }
        });
    }
    //Future中的其他方法转发给computation
    @Override
    public void run() {
        computation.run();
    }
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return computation.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return computation.isCancelled();
    }

    @Override
    public boolean isDone() {
        return computation.isDone();
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return computation.get();
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return computation.get(timeout, unit);
    }
}
