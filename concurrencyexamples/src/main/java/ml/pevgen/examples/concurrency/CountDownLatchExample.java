package ml.pevgen.examples.concurrency;

import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * We may use CountDownLatch,
 * when we have to do some works (for example, load several resources) before start something else
 *
 * Example: 'load' 5 resources (SpecialResource) and then start something that waited ALL (5) resources
 */
public class CountDownLatchExample {

    private static int RESOURCE_COUNT = 5;
    private static CountDownLatch latch = new CountDownLatch(RESOURCE_COUNT);


    private static class SpecialResource implements Runnable {

        int index = 0;

        SpecialResource(int index) {
            this.index = index;
        }

        public void run() {
            System.out.println(LocalTime.now() + " Resource is loading... index - " + index);
            try {
                int time = new Random().nextInt(10) * 1000;
                System.out.println("index - " + index + ";time =" + time);
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
            System.out.println(LocalTime.now() + " Resource has been loaded (latch.countDown)... index - " + index);
        }
    }

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < RESOURCE_COUNT; i++) {
            new Thread(new SpecialResource(i)).start();
        }
        System.out.println(LocalTime.now() + " Before await...");
        latch.await();
        System.out.println(LocalTime.now()
                + " Start something else, what needs all resources - After await... (when all threads executed countDown )");

    }


}
