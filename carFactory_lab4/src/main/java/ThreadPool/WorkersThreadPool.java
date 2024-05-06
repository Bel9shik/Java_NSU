package ThreadPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkersThreadPool{
    BlockingQueue<RunnableThread> workQueue;
    BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();

    AtomicInteger taskCount = new AtomicInteger(0);

    public WorkersThreadPool(int workersQuantity) {
        workQueue = new ArrayBlockingQueue<>(workersQuantity);

        for (int i = 0; i < workersQuantity; i++) {
            workQueue.offer(new RunnableThread(taskQueue, taskCount));
        }

        workQueue.forEach((x) -> new Thread(x).start());
    }


    public synchronized void addTask(Runnable task) {
        taskQueue.offer(task);
        taskCount.incrementAndGet();
    }

    public void shutdown() {
        workQueue.forEach(RunnableThread::shutdown);
    }

    public int getTaskCount() {
        return taskCount.get();
    }
}
