package ThreadPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkersThreadPool{
    BlockingQueue<Thread> workQueue;
    BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();

    AtomicInteger taskCount = new AtomicInteger(0);

    public WorkersThreadPool(int workersQuantity) {
        workQueue = new ArrayBlockingQueue<>(workersQuantity);

        for (int i = 0; i < workersQuantity; i++) {
            workQueue.add(Thread.ofPlatform().start(new RunnableThread(taskQueue, taskCount)));
        }

    }


    public synchronized void addTask(Runnable task) {
        taskQueue.offer(task);
        taskCount.incrementAndGet();
    }

    public void shutdown() {
        workQueue.forEach(Thread::interrupt);
    }

}
