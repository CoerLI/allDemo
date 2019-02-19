package concurrent;

public class TwoThreadPrint {
    int num = 0;
    int max = 100;

    public synchronized void pirnt_2() {
        while(num < max){
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
        while(num < max){
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

    public static void main(String[] args) throws Exception{
        TwoThreadPrint printer = new TwoThreadPrint();
        Thread t1 = new Thread(()->{printer.pirnt_1();});
        t1.setName("t1");
        Thread t2 = new Thread(()->{printer.pirnt_2();});
        t2.setName("t2");
        t1.start();
        Thread.sleep(100);
        t2.start();
    }
}