package net.jcip.examples.chapter10;

import java.util.Random;
import net.jcip.examples.chapter10.DynamicOrderDeadLock.*;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/11 17:36
 * 在典型条件下会发生死锁的循环
 */
public class DemonstrateDeadlock {
    private static final int NUM_THREADS = 20;
    private static final int NUM_ACCOUNTS = 5;
    private static final int NUM_ITERATIONS = 1000000;
    public static void main(String[] args){
        final Random rnd = new Random();
        final Account[] accounts = new Account[NUM_ACCOUNTS];
        for(int i=0; i<accounts.length; i++){
            accounts[i] = new Account(new DollarAmount(100000));
        }
        class TransferThread extends Thread{
            @Override
            public void run() {
                for(int i=0; i<NUM_ITERATIONS; i++){
                    int fromAcct = rnd.nextInt(NUM_ACCOUNTS);
                    int toAcct = rnd.nextInt(NUM_ACCOUNTS);
                    DollarAmount amount = new DollarAmount(rnd.nextInt(1000));
                    try{
                        DynamicOrderDeadLock.transferMoney(accounts[fromAcct], accounts[toAcct], amount);
                    }catch (InsufficientFundsException ignored) {

                    }
                }
            }
        }
        for(int i=0; i<NUM_THREADS; i++){
            new TransferThread().start();
        }
    }
}
