package concurrent;

import java.util.concurrent.atomic.AtomicInteger;

public interface PickNumber {
    int pickNumber();
}

class PickNumberSync implements PickNumber {
    private int number = 1;
    // 每次pick都会加锁

    public synchronized int pickNumber() {
        // number++是非原子操作，可能会被中断，因此可能线程不安全
        // number = number + 1
        // 一次读操作，一次运算，一次赋值
        return number++;
    }

    public PickNumberSync() {
    }
}

class PickNumberAtomicly implements PickNumber {
    private AtomicInteger number = new AtomicInteger(1);

    //  juc包包含原子操作类，通过cas实现原子操作，因此线程安全
    public int pickNumber() {
        return number.getAndIncrement();
    }

    public PickNumberAtomicly() {
    }
}
