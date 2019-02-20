package concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ABCPrinterByCondition {
    ReentrantLock lock = new ReentrantLock();
    Condition notA = lock.newCondition();
    Condition notB = lock.newCondition();
    Condition notC = lock.newCondition();

    int count = 1;
    int max = 6;

    public void print_a() {
        while (count <= max) {
            lock.lock();
            while (count % 3 != 1) {
                try {
                    notA.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (count <= max)
                System.out.println(Thread.currentThread().getName() + "   a");
            count++;
            notB.signal();
            lock.unlock();
        }
    }

    public void print_b() {
        while (count <= max) {
            lock.lock();
            while (count % 3 != 2) {
                try {
                    notB.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (count <= max)
                System.out.println(Thread.currentThread().getName() + "   b");
            count++;
            notC.signal();
            lock.unlock();
        }
    }

    public void print_c() {
        while (count <= max) {
            lock.lock();
            while (count % 3 != 0) {
                try {
                    notC.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (count <= max)
                System.out.println(Thread.currentThread().getName() + "   c");
            count++;
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
