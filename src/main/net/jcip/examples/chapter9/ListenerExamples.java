package net.jcip.examples.chapter9;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/11 16:05
 */
public class ListenerExamples {

    private static ExecutorService exec = Executors.newCachedThreadPool();

    private final JButton colorButton = new JButton("Change color");
    private final Random random = new Random();

    private void backgroundRandom(){
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colorButton.setBackground(new Color(random.nextInt()));
            }
        });
    }
    private final JButton computeButton = new JButton("Big computation");
    private void longRunningTask(){
        computeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exec.execute(new Runnable() {
                    @Override
                    public void run() {
                        /*Do big computation in thread pool*/
                    }
                });
            }
        });
    }

    private final JButton button = new JButton("Do");
    private final JLabel label = new JLabel("idle");

    private void longRunningTaskWithFeedback(){
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button.setEnabled(false);
                label.setText("busy");
                exec.execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            /*Do big computation*/
                        }finally {
                            GuiExecutor.instance().execute(new Runnable() {
                                @Override
                                public void run() {
                                    button.setEnabled(true);
                                    label.setText("idle");
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private final JButton startButton = new JButton("Start");
    private final JButton cancelButton = new JButton("Cancel");
    private Future<?> runningTask = null; //线程封闭
    private void taskWithCancellation(){
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(runningTask != null){
                    runningTask = exec.submit(new Runnable() {
                        @Override
                        public void run() {
                            while(moreWork()){
                                if(Thread.currentThread().isInterrupted()){
                                    cleanUpPartialWork();
                                    break;
                                }
                                doSomeWork();
                            }
                        }

                        private boolean moreWork(){
                            return false;
                        }
                        private void cleanUpPartialWork(){

                        }
                        private void doSomeWork(){

                        }
                    });
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(runningTask!= null){
                    runningTask.cancel(true);
                }
            }
        });
    }

    private void runInBackground(final Runnable task){
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                class CancelListener implements ActionListener{
                    BackgroundTask<?> task;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(task!= null){
                            task.cancel(true);
                        }
                    }
                }
                final CancelListener listener = new CancelListener();
                listener.task = new BackgroundTask<Void>() {
                    @Override
                    protected Void compute() throws Exception {
                        while (moreWork() && !isCancelled()){
                            doSomeWork();
                        }
                        return null;
                    }
                    private boolean moreWork() {
                        return false;
                    }

                    private void doSomeWork() {
                    }

                    @Override
                    protected void onCompletion(Void result, Throwable throwable, boolean canceled) {
                        cancelButton.removeActionListener(listener);
                        label.setText("done");
                    }
                };
                cancelButton.addActionListener(listener);
                exec.execute(task);
            }
        });
    }

}
