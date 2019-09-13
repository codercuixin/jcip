package net.jcip.examples.chapter10;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/11 16:59
 * 锁定顺序死锁。
 */
public class DynamicOrderDeadLock {

    public static void transferMoney(Account fromAccount, Account toAccount, DollarAmount amount) throws InsufficientFundsException {
        synchronized (fromAccount) {
            synchronized (toAccount) {
                if (fromAccount.getBalance().compareTo(amount) < 0) {
                    throw new InsufficientFundsException();
                } else {
                    fromAccount.debit(amount);
                    toAccount.credit(amount);
                }
            }
        }
    }

    public static void main(String[] args) {
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
                        transferMoney(fromAccount, toAccount, dollarAmount);
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
                        transferMoney(toAccount, fromAccount, dollarAmount);
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
