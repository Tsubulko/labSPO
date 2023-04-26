import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {
    private static final int ARRAY_SIZE = 10;
    private static final int MAX_VALUE = 500;
    private static final int SEMAPHORE_PERMITS = 1;

    private static int[] array = new int[ARRAY_SIZE];
    private static int result = 1;

    private static Semaphore semaphore = new Semaphore(SEMAPHORE_PERMITS);

    public static void main(String[] args) throws InterruptedException {
   
            Thread thread1 = new Thread(() -> {
    try {
        semaphore.acquire(); 
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    Random random = new Random();
    for (int i = 0; i < ARRAY_SIZE; i++) {
        int number = random.nextInt(MAX_VALUE + 1);
        array[i] = number;
        System.out.println("Поток 1: " + number);
        try {
            Thread.sleep(500); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    semaphore.release(); 
});


        Thread thread2 = new Thread(() -> {
            try {
                semaphore.acquire(); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < ARRAY_SIZE; i++) {
                if (array[i] % 2 == 0) {
                    result *= array[i];
                }
            }
            System.out.println("Поток 2: Result = " + result);

            semaphore.release(); 
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }
}
