package concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TwoThreadPrinterByCondition {
    Lock lock = new ReentrantLock();
    Condition not1 = lock.newCondition();
    Condition not2 = lock.newCondition();
    int count = 1;
    int max = 0;

    public TwoThreadPrinterByCondition(int max) {
        this.max = max;
    }

    public void print_1() {
        while (count <= max) {
            lock.lock();
            while (count % 2 != 1) {
                try {
                    not1.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (count <= max)
                System.out.println(Thread.currentThread().getName() + " " + count);
            count++;
            not2.signalAll();
            lock.unlock();
        }
    }

    public void print_2() {
        while (count <= max) {
            lock.lock();
            while (count % 2 != 0) {
                try {
                    not2.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (count <= max)
                System.out.println(Thread.currentThread().getName() + " " + count);
            count++;
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
