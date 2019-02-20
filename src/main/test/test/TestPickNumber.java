import concurrent.PickNumber;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestPickNumber {
    static Logger logger = null;

    @BeforeClass
    public static void init() {
        logger = Logger.getLogger(TestPickNumber.class.getName());
        logger.setLevel(Level.ALL);
    }

    @Test
    public void testPickNumberSync() {
        int count = 100000;
        Class PackNamberSync = null;
        Method method = null;
        try {
            Class pickNumber = Class.forName("concurrent.PickNumberSync");
            Constructor constructor = pickNumber.getConstructor();
            constructor.setAccessible(true);
            testPackNumber((PickNumber) constructor.newInstance(), count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPickNumberAtomicly() {
        int count = 100000;
        Class PackNamberSync = null;
        Method method = null;
        try {
            Class pickNumber = Class.forName("concurrent.PickNumberAtomicly");
            Constructor constructor = pickNumber.getConstructor();
            constructor.setAccessible(true);
            testPackNumber((PickNumber) constructor.newInstance(), count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void PickNumberWithoutSync() {
        int count = 100000;
        Class PackNamberSync = null;
        Method method = null;
        try {
            Class pickNumber = Class.forName("concurrent.PickNumberWithoutSync");
            Constructor constructor = pickNumber.getConstructor();
            constructor.setAccessible(true);
            testPackNumber((PickNumber) constructor.newInstance(), count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testPackNumber(PickNumber pickNumber, int latchCount) {
        long start = System.currentTimeMillis();
        PickNumber pn = pickNumber;
        CountDownLatch latch = new CountDownLatch(latchCount);
        HashMap<Integer, Integer> map = new HashMap<>();
        ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < latchCount; i++) {
            pool.submit(() -> {
                int number = pn.pickNumber();
                synchronized (pn) {
                    if (map.containsKey(number))
                        map.put(number, map.get(number) + 1);
                    else
                        map.put(number, 1);
                }
                latch.countDown();
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        logger.log(Level.INFO, pickNumber.getClass().getName() + " : " + map.size() + " -- " + (end - start));
    }
}
