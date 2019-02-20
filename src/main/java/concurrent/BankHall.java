package concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

class Ticket {
    String machineName;
    int number;
    int remainNo;

    public Ticket(String MachineName, int number, int remainNo) {
        this.machineName = MachineName;
        this.number = number;
        this.remainNo = remainNo;
    }
}

public class BankHall {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private int ticketNumber = 1;
    private int max;
    private BlockingQueue<Ticket> queue = new LinkedBlockingQueue();
    Lock lock = new ReentrantLock();

    public BankHall(int max) {
        this.max = max;
    }

    // 多个出票窗口共同使用一个出票机器
    private synchronized Ticket machineOutTicket() {
        Ticket ticket = new Ticket(Thread.currentThread().getName(), ticketNumber++, queue.size());
        queue.offer(ticket);
        return ticket;
    }

    // 模拟出票窗口每隔一段时间出一张票
    public void getTicket() {
        if (ticketNumber > max)
            return;

        lock.lock();
        Ticket ticket = machineOutTicket();
        logger.log(Level.INFO, ticket.machineName + " 出票, 票号 "
                + ticket.number + " ,前面有 " + ticket.remainNo + " 人");
        lock.unlock();

        try {
            Thread.sleep((int) (Math.random() * 3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getTicket();
    }

    // 不同柜员叫号，不能重复
    private Ticket consumeTicket() {
        Ticket ticket = null;
        try {

            ticket = queue.poll(3000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ticket;
    }

    // 柜员叫号，服务一段时间后，再次叫号
    public void doService() {
        Ticket ticket = consumeTicket();
        if(ticket == null)
            return ;
        logger.log(Level.INFO, Thread.currentThread().getName()
                + " 正在服务 顾客 " + ticket.number + ",还有 " + queue.size() + " 人等待");

        try {
            Thread.sleep((int) (Math.random() * 2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        doService();
    }

    public static void main(String[] args) {
        BankHall bankHall = new BankHall(20);
        int server = 3;
        int machine = 5;
        for (int i = 0; i < server; i++) {
            Thread t = new Thread(() -> {
                bankHall.doService();
            });
            t.setName("柜员 " + i);
            t.start();
        }
        for (int i = 0; i < machine; i++) {
            Thread t = new Thread(() -> {
                bankHall.getTicket();
            });
            t.setName("出票窗口 " + i);
            t.start();
        }
    }
}