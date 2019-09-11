package net.jcip.examples.chapter10;

import net.jcip.examples.chapter7.IndexingService;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import static net.jcip.examples.chapter10.DynamicOrderDeadLock.*;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/11 17:24
 * 引入有序锁来避免死锁
 */
public class InduceLockOrder {
    private static final Object tieLock = new Object();

    public void transferMoney(final Account fromAccount, final Account toAccoount,
                              final DollarAmount amount) throws InsufficientFundsException {
        class Helper{
            public void transfer() throws InsufficientFundsException {
                if(fromAccount.getBalance().compareTo(amount) < 0){
                    throw new InsufficientFundsException();
                }
                else{
                    fromAccount.debit(amount);
                    toAccoount.credit(amount);
                }
            }
        }
        int fromHash = System.identityHashCode(fromAccount);
        int toHash = System.identityHashCode(toAccoount);
        //优先获取hash值大的锁，从而使得获取锁的顺序有序。
        if(fromHash < toHash){
            synchronized (fromAccount){
                synchronized (toAccoount){
                    new Helper().transfer();
                }
            }
        }else if(fromHash > toHash){
            synchronized (toAccoount){
                synchronized (fromAccount){
                    new Helper().transfer();
                }
            }
        }else{
            //如果两个hash值一致，则先获取锁tieLock，这种情况是非常少的。
            synchronized (tieLock){
                synchronized (fromAccount){
                    synchronized (toAccoount){
                        new Helper().transfer();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        InduceLockOrder instance = new InduceLockOrder();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        //循环次数指定多些，容易复现死锁。 如果你那里没出现死锁，可以继续调大些观察
        int maxCycle = 1000000;
        Account fromAccount = new Account(new DollarAmount(10000000));
        Account toAccount = new Account(new DollarAmount(10000000));
        DollarAmount dollarAmount = new DollarAmount(1);

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
                        instance.transferMoney(fromAccount, toAccount, dollarAmount);
                    } catch (InsufficientFundsException e) {
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
                        instance.transferMoney(toAccount, fromAccount, dollarAmount);
                    } catch (InsufficientFundsException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        leftRightThread.start();
        rightLeftThread.start();
        countDownLatch.countDown();
    }
}
