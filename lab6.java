import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String[] args) {
        final int n = 10;
        final int max = 500;

        int[] arr = new int[n];
        Semaphore sem = new Semaphore(0);

        Thread producer = new Thread(() -> {
            Random rand = new Random();
            for (int i = 0; i < n; i++) {
                arr[i] = rand.nextInt(max + 1);
                System.out.println("Producer: added " + arr[i]);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            sem.release();
        });

        Thread consumer = new Thread(() -> {
            long prod = 1;
            try {
                sem.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < n; i++) {
                if (arr[i] % 2 == 0) {
                    prod *= arr[i];
                    System.out.println("Consumer: multiplied by " + arr[i] + ", current product = " + prod);
                }
            }
        });

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
