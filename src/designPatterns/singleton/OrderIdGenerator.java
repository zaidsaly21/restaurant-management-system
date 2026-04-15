package designPatterns.singleton;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Simple sequential ID generator (Singleton).
 */
public class OrderIdGenerator {

    private final AtomicLong seq = new AtomicLong(1);

    private OrderIdGenerator() {}

    private static class Holder {
        private static final OrderIdGenerator INSTANCE = new OrderIdGenerator();
    }

    public static OrderIdGenerator getInstance() {
        return Holder.INSTANCE;
    }

    public long nextId() {
        return seq.getAndIncrement();
    }
}