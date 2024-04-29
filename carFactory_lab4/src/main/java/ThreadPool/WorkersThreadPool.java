package ThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class WorkersThreadPool implements Runnable {
    private final BlockingDeque<Runnable> workersQueue;
    public List<Thread> workerList;

    public WorkersThreadPool(int workersQuantity) {
        workersQueue = new LinkedBlockingDeque<>();
        workerList = new ArrayList<>(workersQuantity);
    }

    public synchronized void addTask(Runnable runnable) {
        workersQueue.offer(runnable);
        notifyAll();
    }

    @Override
    public void run() {
        synchronized (this) {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    while (!workersQueue.isEmpty()) {
                        for (Runnable runnable : workersQueue) {
                            if ()
                        }
                    }
                    Runnable runnable = workersQueue.take();
                    Thread.State state = workerList.get(0).getState();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
