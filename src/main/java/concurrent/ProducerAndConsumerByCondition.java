package concurrent;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

class Consumer implements Runnable {
    ProducerAndConsumerByCondition pacs = null;

    public Consumer(ProducerAndConsumerByCondition pacs) {
        this.pacs = pacs;
    }

    @Override
    public void run() {
        pacs.consumerObject();
    }
}

class Producer implements Runnable {
    ProducerAndConsumerByCondition pacs = null;

    public Producer(ProducerAndConsumerByCondition pacs) {
        this.pacs = pacs;
    }

    @Override
    public void run() {
        pacs.produceObject();
    }
}

public class ProducerAndConsumerByCondition {
    Logger logger = Logger.getLogger(this.getClass().getName());
    private Queue<Object> repository = new LinkedList<Object>();
    //规定仓库大小
    private final int max = 10;
    private Lock lock = new ReentrantLock();
    private Condition isEmpty = lock.newCondition();
    private Condition isFull = lock.newCondition();

    public void consumerObject() {
        lock.lock();
        while (repository.isEmpty()) {
            try {
                isEmpty.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        repository.poll();
        isFull.signalAll();
        logger.log(Level.INFO, "consume an object, current size is " + repository.size());
        lock.unlock();
    }

    public void produceObject() {
        lock.lock();
        while (repository.size() >= max) {
            try {
                isFull.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        repository.offer(new Object());
        isEmpty.signalAll();
        logger.log(Level.INFO, "produce an object, current size is " + repository.size());
        lock.unlock();
    }

    public static void main(String[] args) {
        ProducerAndConsumerByCondition pavs = new ProducerAndConsumerByCondition();
        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < 50; i++) {
            es.execute(new Consumer(pavs));
            es.execute(new Producer(pavs));
        }
        es.shutdown();
    }

}
