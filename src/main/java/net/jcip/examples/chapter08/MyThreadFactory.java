package net.jcip.examples.chapter08;

import javax.management.relation.RoleUnresolved;
import java.util.concurrent.*;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/10 19:59
 */
public class MyThreadFactory implements ThreadFactory{
    private final String poolName;
    public MyThreadFactory(String poolName){
        this.poolName = poolName;
    }
    @Override
    public Thread newThread(Runnable r) {
       return new MyAppThread(r, poolName);
    }

    public static void main(String[] args){
        //假设是计算密集型算法
        int processors = Runtime.getRuntime().availableProcessors();
        int corePoolSize = processors;
        int maxPoolSize = processors +1;
        int maxWaitingTask = 5;
        //只允许maxWaitingTask个任务在队列中。
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(maxWaitingTask);
        //这是默认的策略，对于拒绝的任务会抛出异常
        RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
//        //舍弃最新的任务
//        RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.DiscardPolicy();
        //舍弃掉最老的任务
//        RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.DiscardOldestPolicy();

        //直接在execute 所在的线程中执行任务，这样会限制任务提交的速度。 注意看线程名称。
//        RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();

        MyThreadFactory myThreadFactory = new MyThreadFactory("testPool");
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 60, TimeUnit.SECONDS, blockingQueue, myThreadFactory, rejectedExecutionHandler);
        for(int i= 0; i<100; i++){
            threadPoolExecutor.execute(new Job("job"+i));
        }
        threadPoolExecutor.shutdown();
    }
    //todo 实现自己的RejectedExecutionHandler。
    /**
     * 由于PriorityBlockingQueue 是无边界阻塞队列，添加的元素要实现Comparable接口，大的排在优先级阻塞队列前面。
     */
    public static void testPriorityBlockingQueue(){
        //假设是计算密集型算法
        int processors = Runtime.getRuntime().availableProcessors();
        int corePoolSize = processors;
        int maxPoolSize = processors +1;
        //无界，阻塞，按优先级获取元素，可能OOM
        BlockingQueue<Runnable> blockingQueue = new PriorityBlockingQueue<>(5);
        MyThreadFactory myThreadFactory = new MyThreadFactory("testPool");
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 60, TimeUnit.SECONDS, blockingQueue, myThreadFactory);
        for(int i= 0; i<100; i++){
            threadPoolExecutor.execute(new PriorityJob("job"+i, 100-i));
        }
        threadPoolExecutor.shutdown();
    }
    public static class PriorityJob implements Runnable, Comparable{
        private String jobName;
        private int priority;
        public PriorityJob(String jobName, int priority){
            this.jobName = jobName;
            this.priority = priority;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" has done "+ jobName +", and job priority is "+priority );
        }

        @Override
        public int compareTo(Object o) {
            if( this == o){
                return 0;
            }
            if(o instanceof Job) {
                PriorityJob other = (PriorityJob) o;
                return other.priority - this.priority;
            }else{
                return 0;
            }
        }

    }

    public static class Job implements Runnable{
        private String jobName;
        public Job(String jobName){
            this.jobName = jobName;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" has done "+ jobName );
        }
    }
}
