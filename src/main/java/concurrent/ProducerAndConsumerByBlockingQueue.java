package concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProducerAndConsumerByBlockingQueue {
    private BlockingQueue<Object> queue = new LinkedBlockingQueue<>(10);
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void produceObject() {
        try {
            queue.put(new Object());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.log(Level.INFO, "produce an object, current size is " + queue.size());
    }

    public void consumeObject() {
        try {
            queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.log(Level.INFO, "consume an object, current size is " + queue.size());

    }

    public static void main(String[] args) {
        ProducerAndConsumerByBlockingQueue pavs = new ProducerAndConsumerByBlockingQueue();
        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < 20; i++) {
            es.execute(()->pavs.consumeObject());
            es.execute(()->pavs.produceObject());
        }
        es.shutdown();
    }
}


