package concurrent;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
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

    public void testPackNumber(PickNumber pickNumber, int latchCount) {
        PickNumber pn = pickNumber;

        CountDownLatch latch = new CountDownLatch(latchCount);

        Set<Integer> set = Collections.synchronizedSet(new HashSet());

        ExecutorService pool = Executors.newFixedThreadPool(20);

        for (int i = 0; i < latchCount; i++) {
            pool.submit(() -> {
                int number = pn.pickNumber();
                set.add(number);
                latch.countDown();
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 检查是否有重复
        Assert.assertEquals(latchCount, (long) set.size());

        Object[] array = set.toArray();
        int min = 1;
        int max = latchCount;
        for (int i = 0; i < array.length; i++) {
            min = Math.min(min, (Integer) array[i]);
            max = Math.max(max, (Integer) array[i]);
        }
        // 检查最大值最小值是否正确
        Assert.assertEquals(1, min);
        Assert.assertEquals(latchCount, max);

    }
}
