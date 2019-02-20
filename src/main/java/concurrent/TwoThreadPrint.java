package concurrent;

public class TwoThreadPrint {
    int num = 1;
    int max = 0;

    public TwoThreadPrint(int max) {
        this.max = max;
    }

    // 两个线程交替运行，则在方法内部控制范围
    // 多个线程按一定顺序执行，则可以通过控制线程数来控制范围，参考ABCPrinter
    public synchronized void pirnt_2() {
        while (num < max) {
            while (num % 2 != 0) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + "  " + num);
            num++;
            this.notify();
        }
    }

    public synchronized void pirnt_1() {
        while (num < max) {
            while (num % 2 != 1) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + "  " + num);
            num++;
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