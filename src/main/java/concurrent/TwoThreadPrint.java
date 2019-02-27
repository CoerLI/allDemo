package concurrent;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TwoThreadPrint {
    int cur = 1;
    int max = 0;
    Logger logger = Logger.getLogger(this.getClass().getName());

    public TwoThreadPrint(int max) {
        this.max = max;
    }

    // 两个线程交替运行，在方法内部控制结束条件
    // 多个线程按一定顺序执行，则可以通过控制线程数来控制范围，参考ABCPrinter
    public synchronized void pirnt_2() {
        while (cur < max) {
            while (cur % 2 != 0) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            logger.log(Level.INFO, Thread.currentThread().getName() + "  " + cur);
            cur++;
            this.notify();
        }
    }

    public synchronized void pirnt_1() {
        while (cur < max) {
            while (cur % 2 != 1) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            logger.log(Level.INFO, Thread.currentThread().getName() + "  " + cur);
            cur++;
            this.notify();
        }
    }

    public static void main(String[] args) throws Exception {
        TwoThreadPrint printer = new TwoThreadPrint(100);
        Thread t1 = new Thread(() -> {
            printer.pirnt_1();
        });
        t1.setName("t1");
        Thread t2 = new Thread(() -> {
            printer.pirnt_2();
        });
        t2.setName("t2");
        t1.start();
        Thread.sleep(100);
        t2.start();
    }
}