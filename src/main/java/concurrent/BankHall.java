package concurrent;

import java.util.concurrent.*;
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

interface Business {
    int getConsumeTime();

    String getBusinessType();

}

class Business_takeMoney implements Business {
    private int baseTime = 500;
    private static final String BUSINESSTYPE = "取款";

    public int getConsumeTime() {
        return baseTime + (int) (Math.random() * 100);
    }

    @Override
    public String getBusinessType() {
        return BUSINESSTYPE;
    }

}

class Business_saveMoney implements Business {
    private int baseTime = 700;
    private static final String BUSINESSTYPE = "存款";

    public int getConsumeTime() {
        return baseTime + (int) (Math.random() * 100);
    }

    @Override
    public String getBusinessType() {
        return BUSINESSTYPE;
    }
}

class Business_transferMoney implements Business {
    private int baseTime = 900;
    private static final String BUSINESSTYPE = "转账";

    public int getConsumeTime() {
        return baseTime + (int) (Math.random() * 100);
    }

    @Override
    public String getBusinessType() {
        return BUSINESSTYPE;
    }
}

public class BankHall {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private int ticketNumber = 1;
    // 服务柜台
    private ExecutorService serverThreadPool;
    // 出票窗口
    private ExecutorService ticketWindowThreadPool;
    private BlockingQueue<Ticket> queue = new LinkedBlockingQueue();
    // Lock lock = new ReentrantLock();

    public BankHall(int serverNum, int ticketWindow) {
        serverThreadPool = Executors.newFixedThreadPool(serverNum);
        ticketWindowThreadPool = Executors.newFixedThreadPool(ticketWindow);
    }

    // 多个出票窗口共同使用一个出票机器
    private synchronized Ticket machineOutTicket() {
        Ticket ticket = new Ticket(Thread.currentThread().getName(), ticketNumber++, queue.size());
        queue.offer(ticket);
        logger.log(Level.INFO, ticket.machineName + " 出票, 票号 "
                + ticket.number + " ,前面有 " + ticket.remainNo + " 人");
        return ticket;
    }

    // 模拟出票窗口每隔一段时间出一张票
    public void pickTicket() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                Ticket ticket = machineOutTicket();
            }
        };
        ticketWindowThreadPool.execute(task);
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
    public void doService(Business business) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                Ticket ticket = consumeTicket();
                logger.log(Level.INFO, Thread.currentThread().getName() + "正在服务 " + ticket.number + " 号顾客，还有 " + queue.size() + " 位顾客等待");
                try {
                    Thread.sleep(business.getConsumeTime());
                    logger.log(Level.INFO, ticket.number + " 号顾客办理 " + business.getBusinessType() + "业务 完毕，用时" + business.getConsumeTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        serverThreadPool.execute(task);
    }

    public void shutDown(int secounds) {

//        ticketWindowThreadPool.shutdown();
        try {//等待直到所有任务完成
            ticketWindowThreadPool.awaitTermination(secounds, TimeUnit.SECONDS);
            ticketWindowThreadPool.shutdownNow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        serverThreadPool.shutdown();
        try {//等待直到所有任务完成
            serverThreadPool.awaitTermination(secounds, TimeUnit.SECONDS);
            serverThreadPool.shutdownNow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Customer extends Thread {
    BankHall bankHall;

    public Customer(BankHall bankHall) {
        this.bankHall = bankHall;
    }


    @Override
    public void run() {
        bankHall.pickTicket();
        bankHall.doService(swichRandomBusiness());
    }

    public Business swichRandomBusiness() {
        int random = (int) (Math.random() * 3);
        switch (random) {
            case 0:
                return new Business_takeMoney();
            case 1:
                return new Business_saveMoney();
            case 2:
                return new Business_transferMoney();
            default:
                return new Business_takeMoney();
        }
    }
}