package util;

import lombok.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author naison
 * @since 3/25/2020 17:21
 */
public class ThreadUtil {
    private static final ExecutorService POOL = new ThreadPoolExecutor(
            4,
            8,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new KVSThreadFactory("TASK"),
            (r, executor) -> System.out.println("the task queue is full, i am sorry for can't let you come in" + r));

    private static final ScheduledExecutorService SCHEDULED_POOL = new ScheduledThreadPoolExecutor(
            8,
            new KVSThreadFactory("SCHEDULE"),
            (r, executor) -> System.out.println("the schedule queue is full, i am sorry for can't let you come in" + r));

    public static ExecutorService getThreadPool() {
        return POOL;
    }

    public static ScheduledExecutorService getScheduledThreadPool() {
        return SCHEDULED_POOL;
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class KVSThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public KVSThreadFactory(String poolName) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = poolName + ":" + poolNumber.getAndIncrement() + ":thread:";
        }

        public Thread newThread(@NonNull Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY) t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
