package ml.pevgen.examples.concurrency;

public class PingPongWaitNotify {

    static final Object lock = new Object();

    static class Ping implements Runnable {

        public void run() {
            synchronized (lock) {
                for (int i = 0; i < 10; i++) {
                    System.out.println("Ping " + i);
                    lock.notify();
                    if (i < 9) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    static class Pong implements Runnable {

        public void run() {
            synchronized (lock) {
                for (int i = 0; i < 10; i++) {
                    System.out.println("Pong " + i);
                    lock.notify();
                    if (i < 9) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new Ping());
        Thread t2 = new Thread(new Pong());

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
