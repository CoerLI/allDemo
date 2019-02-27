package concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TwoThreadPrinterByCondition {
    Lock lock = new ReentrantLock();
    Condition not1 = lock.newCondition();
    Condition not2 = lock.newCondition();
    int cur = 1;
    int max = 0;
    Logger logger = Logger.getLogger(this.getClass().getName());

    public TwoThreadPrinterByCondition(int max) {
        this.max = max;
    }

    public void print_1() {
        while (cur <= max) {
            lock.lock();
            while (cur % 2 != 1) {
                try {
                    not1.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (cur <= max)
                logger.log(Level.INFO, Thread.currentThread().getName() + " " + cur);
            cur++;
            not2.signalAll();
            lock.unlock();
        }
    }

    public void print_2() {
        while (cur <= max) {
            lock.lock();
            while (cur % 2 != 0) {
                try {
                    not2.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (cur <= max)
                logger.log(Level.INFO, Thread.currentThread().getName() + " " + cur);
            cur++;
            not1.signalAll();
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        TwoThreadPrinterByCondition printer = new TwoThreadPrinterByCondition(10);
        Thread t1 = new Thread(() -> printer.print_1());
        Thread t2 = new Thread(() -> printer.print_2());
        t1.start();
        t2.start();
    }
}
