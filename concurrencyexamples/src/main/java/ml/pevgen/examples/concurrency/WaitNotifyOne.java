package ml.pevgen.examples.concurrency;

/**
 * wait 3 thread - 1 notify
 * Only one waiting thread will be notified and wake up
 */
public class WaitNotifyOne {

    static final Object lock = new Object();
    static volatile boolean stop = false;

    static class WaitThread implements Runnable {
        final int threadIndex;

        WaitThread(int threadIndex) {
            this.threadIndex = threadIndex;
        }

        public void run() {
            synchronized (lock) {
                System.out.println("Start waiting.... - " + threadIndex);
                try {
                    while (!stop) {
                        lock.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Stop waiting.... - " + threadIndex);
            }
        }
    }

    static class NotifyThread implements Runnable {
        final int threadIndex;

        NotifyThread(int threadIndex) {
            this.threadIndex = threadIndex;
        }

        public void run() {
            synchronized (lock) {
                System.out.println("Start notify.... - " + threadIndex);
                stop = true;
                lock.notify();
                System.out.println("Stop notify.... - " + threadIndex);
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new Thread(new WaitThread(i)).start();
        }
        Thread tn = new Thread(new NotifyThread(999));

        tn.start();
        try {
            tn.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        Result (for example)

//        Start waiting.... - 0
//        Start waiting.... - 2
//        Start waiting.... - 1
//        Start notify.... - 999
//        Stop notify.... - 999
//        Stop waiting.... - 0

//     - java is still waiting
    }
}
