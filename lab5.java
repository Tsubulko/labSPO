import java.util.Random;

public class Main {
    private static final int ARRAY_SIZE = 10;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            int[] array = new int[ARRAY_SIZE];
            System.out.println("Array " + (i + 1) + ":");
            fillArray(array);
            printArray(array);
            Thread thread1 = new Thread(() -> {
                Random random = new Random();
                for (int j = 0; j < ARRAY_SIZE; j++) {
                    synchronized (array) {
                        array[j] = random.nextInt(301) - 150;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Thread 1 updated array[" + j + "]: " + array[j]);
                    }
                }
            });
            Thread thread2 = new Thread(() -> {
                synchronized (array) {
                    double sum = 0;
                    for (int num : array) {
                        sum += num;
                    }
                    double avg = sum / (ARRAY_SIZE - 1);
                    array[0] = (int) avg;
                    System.out.println("Thread 2 updated array[0]: " + array[0]);
                }
            });
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
            System.out.println("Resulting array " + (i + 1) + ":");
            printArray(array);
        }
    }

    private static void fillArray(int[] array) {
        Random random = new Random();
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = random.nextInt(301) - 150;
        }
    }

    private static void printArray(int[] array) {
        for (int i = 0; i < ARRAY_SIZE; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println();
    }
}
