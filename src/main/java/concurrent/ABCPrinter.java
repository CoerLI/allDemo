package concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ABCPrinter {
    int state = 1;

    public synchronized void print_a() {
        while (state != 1) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName()+"    a");
        state = 2;
        this.notifyAll();
    }

    public synchronized void print_b() {
        while (state != 2) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName()+"    b");
        state = 3;
        this.notifyAll();
    }

    public synchronized void print_c() {
        while (state != 3) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName()+"    c");
        state = 1;
        this.notifyAll();
    }

    public static void main(String[] args) {
        ABCPrinter abc = new ABCPrinter();
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            Thread t1 = new Thread(() -> {
                synchronized (abc) {
                    abc.print_a();
                }
            });
            Thread t2 = new Thread(() -> {
                synchronized (abc) {
                    abc.print_b();
                }
            });
            Thread t3 = new Thread(() -> {
                synchronized (abc) {
                    abc.print_c();
                }
            });
            pool.execute(t1);
            pool.execute(t2);
            pool.execute(t3);
        }
        pool.shutdown();
    }
}