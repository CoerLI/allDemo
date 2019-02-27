package concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ABCPrinterByCondition {
    ReentrantLock lock = new ReentrantLock();
    Condition notA = lock.newCondition();
    Condition notB = lock.newCondition();
    Condition notC = lock.newCondition();
    Logger logger = Logger.getLogger(this.getClass().getName());
    int cur = 1;
    int max = 6;

    public void print_a() {
        while (cur <= max) {
            lock.lock();
            while (cur % 3 != 1) {
                try {
                    notA.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (cur <= max)
                logger.log(Level.INFO, Thread.currentThread().getName() + "   a");
            cur++;
            notB.signal();
            lock.unlock();
        }
    }

    public void print_b() {
        while (cur <= max) {
            lock.lock();
            while (cur % 3 != 2) {
                try {
                    notB.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (cur <= max)
                logger.log(Level.INFO, Thread.currentThread().getName() + "   b");
            cur++;
            notC.signal();
            lock.unlock();
        }
    }

    public void print_c() {
        while (cur <= max) {
            lock.lock();
            while (cur % 3 != 0) {
                try {
                    notC.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (cur <= max)
                logger.log(Level.INFO, Thread.currentThread().getName() + "   c");
            cur++;
            notA.signal();
            lock.unlock();
        }
    }

    public ABCPrinterByCondition(int max) {
        this.max = max;
    }

    public static void main(String[] args) throws Exception {
        ABCPrinterByCondition printer = new ABCPrinterByCondition(15);
        Thread a = new Thread(() -> printer.print_a());
        Thread b = new Thread(() -> printer.print_b());
        Thread c = new Thread(() -> printer.print_c());
        a.start();
        Thread.sleep(100);
        b.start();
        Thread.sleep(100);
        c.start();
    }
}
