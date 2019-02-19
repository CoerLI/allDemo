package concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ABCPrinterByCondition {
    ReentrantLock lock = new ReentrantLock();
    Condition notA = lock.newCondition();
    Condition notB = lock.newCondition();
    Condition notC = lock.newCondition();

    int count = 1;
    final int max = 6;

    public void print_a() {
        lock.lock();
        while (count <= max) {
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
            notB.signalAll();
        }
        lock.unlock();
    }

    public void print_b() {
        lock.lock();
        while (count <= max) {
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
            notC.signalAll();
        }
        lock.unlock();
    }

    public void print_c() {
        lock.lock();
        while (count <= max) {
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
            notA.signalAll();
        }
        lock.unlock();
    }

    public static void main(String[] args) throws Exception {
        ABCPrinterByCondition printer = new ABCPrinterByCondition();
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
