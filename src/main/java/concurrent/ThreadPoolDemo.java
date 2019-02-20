package concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThreadPoolDemo {
    public static void main(String[] args) {
        Executor pool = Executors.newFixedThreadPool(5);

    }
}
