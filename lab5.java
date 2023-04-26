import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

public class Main {
    private static int[] array = new int[10];
    private static Mutex mutex = new Mutex();

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            array = new int[10];
            Thread thread1 = new Thread(new FillArrayTask());
            Thread thread2 = new Thread(new ModifyArrayTask());
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
            System.out.println("Массив " + (i + 1) + ": " + java.util.Arrays.toString(array));
        }
    }

    private static class FillArrayTask implements Runnable {
        @Override
        public void run() {
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                mutex.lock();
                try {
                    array[i] = random.nextInt(100) + 1;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutex.unlock();
                }
            }
        }
    }

    private static class ModifyArrayTask implements Runnable {
        @Override
        public void run() {
            mutex.lock();
            try {
                double sum = 0;
                for (int i = 0; i < 10; i++) {
                    sum += array[i];
                }
                double average = sum / 10;
                array[0] = (int) average;
                for (int i = 1; i < 10; i++) {
                    array[i] *= 2;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mutex.unlock();
            }
        }
    }

    private static class Mutex {
        private boolean locked = false;
        private Thread owner = null;
        private int count = 0;

        public synchronized void lock() throws InterruptedException {
            Thread callingThread = Thread.currentThread();
            while (locked && owner != callingThread) {
                wait();
            }
            locked = true;
            owner = callingThread;
            count++;
        }

        public synchronized void unlock() {
            if (Thread.currentThread() != owner) {
                throw new IllegalMonitorStateException("Calling thread does not hold the lock");
            }
            count--;
            if (count == 0) {
                locked = false;
                owner = null;
                notify();
            }
        }
    }
}
