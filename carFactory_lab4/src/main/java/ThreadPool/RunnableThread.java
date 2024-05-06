package ThreadPool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class RunnableThread implements Runnable {
    BlockingQueue<Runnable> taskQueue;
    AtomicInteger counter;

    public RunnableThread(BlockingQueue<Runnable> taskQueue, AtomicInteger counter) {
        this.taskQueue = taskQueue;
        this.counter = counter;
    }

    public void shutdown() {
        Thread.currentThread().interrupt();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                taskQueue.take().run();
                counter.decrementAndGet();
            } catch (InterruptedException ignored) {}
        }
    }
}
