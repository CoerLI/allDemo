package concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadPoolDemo {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(new ThreadPoolDemo().getClass().getName());

        ExecutorService pool = Executors.newFixedThreadPool(10);
//        ExecutorService pool2 = Executors.newScheduledThreadPool(10);

        for (int i = 0; i < 200; i++) {
            pool.execute(() -> {
                logger.log(Level.INFO, Thread.currentThread().getName() + "  is running ...");
            });
        }
        pool.shutdown();
    }
}
