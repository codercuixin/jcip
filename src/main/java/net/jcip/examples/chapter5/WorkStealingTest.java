package net.jcip.examples.chapter5;

import java.util.Deque;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/19 15:30
 * 借助Deque 双端队列实现work Stealing。
 */
public class WorkStealingTest {
    public static void main(String[] args) {
        Deque<Task> taskGroup1 = new LinkedBlockingDeque<>();
        Deque<Task> taskGroup2 = new LinkedBlockingDeque<>();
        taskGroup1.add(new Task("taskGroup1-task1", 100));
        taskGroup1.add(new Task("taskGroup1-task2", 100));
        taskGroup2.add(new Task("taskGroup2-task1", 100));
        taskGroup2.add(new Task("taskGroup2-task2", 100));
        taskGroup2.add(new Task("taskGroup2-task3", 100));
        taskGroup2.add(new Task("taskGroup2-task4", 100));
        workSteal(taskGroup1, taskGroup2);

    }

    public static void workSteal(Deque<Task> taskGroup1, Deque<Task> taskGroup2) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Task task;
                while ((task = taskGroup1.pollFirst()) != null) {
                    task.run();
                }
                while ((task = taskGroup2.pollLast()) != null) {
                    task.run();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Task task;
                while ((task = taskGroup2.pollFirst()) != null) {
                    task.run();
                }
                while ((task = taskGroup1.pollLast()) != null) {
                    task.run();
                }
            }
        }).start();

    }

    static class Task implements Runnable {
        private String name;
        private int sleepMs;

        public Task(String name, int sleepMs) {
            this.name = name;
            this.sleepMs = sleepMs;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(sleepMs);
                System.out.printf("threadName=%s, taskName=%s\n", Thread.currentThread().getName(), name);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
