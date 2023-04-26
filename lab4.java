import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int[] arr = new int[10];
        Thread thread1 = new Thread(new RandomFiller(arr));
        Thread thread2 = new Thread(new ArrayChecker(arr));
        thread1.start();
        thread2.start();
    }
}

class RandomFiller implements Runnable {
    private int[] arr;
    private Random random = new Random();

    public RandomFiller(int[] arr) {
        this.arr = arr;
    }

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < arr.length; i++) {
                arr[i] += random.nextInt(301) - 150;
            }
            System.out.println("Array: " + java.util.Arrays.toString(arr));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class ArrayChecker implements Runnable {
    private int[] arr;

    public ArrayChecker(int[] arr) {
        this.arr = arr;
    }

    @Override
    public void run() {
        while (true) {
            double sum = 0;
            for (int i = 0; i < arr.length; i++) {
                sum += arr[i];
            }
            double avg = sum / arr.length;
            if (arr[0] != avg) {
                arr[0] = (int) avg;
                System.out.println("Array changed: " + java.util.Arrays.toString(arr));
            }
        }
    }
}
