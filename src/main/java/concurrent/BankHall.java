package concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

class Ticket {
    String MachineName;
    int number;
    int remainNo;

    public Ticket(String MachineName, int number, int remainNo) {
        this.MachineName = MachineName;
        this.number = number;
        this.remainNo = remainNo;
    }
}

public class BankHall {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private int ticketCount = 1;
    private int serviceCount = 1;
    private int max;
    private BlockingQueue<Ticket> queue = new LinkedBlockingQueue();
    Lock lock = new ReentrantLock();

    public BankHall(int max) {
        this.max = max;
    }

    // 多个出票窗口共同使用一个出票机器
    private synchronized Ticket machineOutTicket() {
        Ticket ticket = new Ticket(Thread.currentThread().getName(), ticketCount++, queue.size());
        queue.offer(ticket);
        return ticket;
    }

    // 模拟出票窗口每隔一段时间出一张票
    public void getNumber() {
        if (ticketCount > max)
            return;
        lock.lock();
        Ticket ticket = machineOutTicket();
        logger.log(Level.INFO, ticket.MachineName + " 出票, 票号 "
                + ticket.number + " ,前面有 " + ticket.remainNo + " 人");
        lock.unlock();
        try {
            Thread.sleep((int) (Math.random() * 10000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getNumber();
    }

    // 不同柜员叫号，不能重复
    private Ticket consumeTicket() {
        Ticket ticket = null;
        try {
            ticket = queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ticket;
    }

    // 柜员叫号，服务一段时间后，再次叫号
    public void service() {
        Ticket ticket = consumeTicket();
        logger.log(Level.INFO, Thread.currentThread().getName()
                + " 正在服务 顾客 " + ticket.number + ",还有 " + queue.size() + " 人等待");
        try {
            Thread.sleep((int) (Math.random() * 10000));
            serviceCount++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        service();
    }

    public static void main(String[] args) {
        BankHall bankHall = new BankHall(20);
        int server = 3;
        int machine = 5;
        for (int i = 0; i < server; i++) {
            Thread t = new Thread(() -> {
                bankHall.service();
            });
            t.setName("柜员 " + i);
            t.start();
        }
        for (int i = 0; i < machine; i++) {
            Thread t = new Thread(() -> {
                bankHall.getNumber();
            });
            t.setName("出票窗口 " + i);
            t.start();
        }
    }
}