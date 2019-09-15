package net.jcip.examples.chapter12;

import net.jcip.examples.chapter10.DynamicOrderDeadLock;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * @author cuixin on 2019-09-15
 * 使用tryLock来避免锁顺序 导致的死锁
 **/
public class DeadLockAvoidance {
    private static Random rnd = new Random();

    public static boolean transferMoney(Account fromAcct, Account toAcct, DollarAmount amount, long timeout, TimeUnit unit) throws InsufficientFundsException, InterruptedException {
       long fixedDelay = getFixedDelayComponentNanos(timeout, unit);
       long randMod = getRandomDelayModulesNanos(timeout, unit);
       long stopTime = System.nanoTime() + unit.toNanos(timeout);
       while (true){
           if(fromAcct.lock.tryLock()){
               try{
                   if(toAcct.lock.tryLock()){
                       try{
                           if(fromAcct.getBalance().compareTo(amount) < 0){
                               throw new InsufficientFundsException();
                           }else{
                               fromAcct.debit(amount);
                               toAcct.credit(amount);
                               return true;
                           }
                       }finally {
                           toAcct.lock.unlock();
                       }
                   }
               }finally {
                   fromAcct.lock.unlock();
               }
           }
           //超过结束时间了就返回
           if(System.nanoTime() > stopTime){
               return false;
           }
           //随机睡一段时间，再尝试获取锁
           NANOSECONDS.sleep(fixedDelay + rnd.nextLong() % randMod);
       }
    }

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        //循环次数指定多些，容易复现死锁。 如果你那里没出现死锁，可以继续调大些观察
        int maxCycle = 1000000;
        Account fromAccount = new Account(new DollarAmount(10000000));
        Account toAccount = new Account(new DollarAmount(10000000));
        DollarAmount dollarAmount = new DollarAmount(1);
        long timeout = 60;
        TimeUnit timeUnit = NANOSECONDS;

        Thread leftRightThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < maxCycle; i++) {
                    try {
                        transferMoney(fromAccount, toAccount, dollarAmount, timeout, timeUnit);
                    } catch (InsufficientFundsException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        Thread rightLeftThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < maxCycle; i++) {
                    try {
                        transferMoney(toAccount, fromAccount, dollarAmount, timeout, timeUnit);
                    } catch (InsufficientFundsException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        leftRightThread.start();
        rightLeftThread.start();
        countDownLatch.countDown();
    }

    private static final int DELAY_FIXED = 1;
    private static final int DELAY_RANDOM = 2;
    static long getFixedDelayComponentNanos(long timeout, TimeUnit unit){
        return DELAY_FIXED;
    }
    static long getRandomDelayModulesNanos(long timeout, TimeUnit unit){
        return DELAY_RANDOM;
    }


    static class DollarAmount implements Comparable<DollarAmount> {
        private int amount;

        public DollarAmount(int amount) {
            this.amount = amount;
        }

        @Override
        public int compareTo(DollarAmount o) {
            return this.amount - o.amount;
        }

        public DollarAmount add(DollarAmount d) {
            return new DollarAmount(this.amount + d.amount);
        }

        public DollarAmount subtract(DollarAmount d) {
            return new DollarAmount(this.amount - d.amount);
        }

        public int getAmount() {
            return amount;
        }
    }

    static class Account {
        public Lock lock = new ReentrantLock();
        private static final AtomicInteger sequence = new AtomicInteger();
        private final int acctNo;
        private DollarAmount balance;

        public Account(DollarAmount balance) {
            acctNo = sequence.incrementAndGet();
            this.balance = balance;
        }

        public DollarAmount getBalance() {
            return balance;
        }

        void debit(DollarAmount d) {
            balance = balance.subtract(d);
        }

        void credit(DollarAmount d) {
            balance = balance.add(d);
        }

        int getAcctNo() {
            return acctNo;
        }
    }
    static class InsufficientFundsException extends Exception {
    }

}
