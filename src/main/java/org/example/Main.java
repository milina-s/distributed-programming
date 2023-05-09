package org.example;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.example.Data.*;

/**
 * Y = D * MT + max(B) * D
 * MA = MT * (MT + MZ) - MZ * MT
 * </p>
 * Matrices: MT, MZ - square matrices [n x n]
 * Vectors: D, B - column vectors [1 x n]
 */

public class Main {

    public static double[][] MT, MZ;
    public static double[][] D, B;
    public static Lock lock = new ReentrantLock(); // Lock for synchronization
    public static BlockingQueue<double[][]> resultQueue = new ArrayBlockingQueue<>(2); // Shared queue to hold equation results



    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        int n = 200;
//        System.out.println("Enter n: ");
//        Scanner scanner = new Scanner(System.in);
//        n = scanner.nextInt();

//        generateData(n);
        readData(n);

        CountDownLatch latch = new CountDownLatch(3); // To wait for both equations to finish

        ForkJoinPool pool = new ForkJoinPool(2); // Create a pool of threads with parallelism level of 2

        // Y = D * MT + max(B) * D
        pool.submit(EquationThreads.firstEquationThread(latch, resultQueue)); // Submit first equation task to the pool
        // MA = MT * (MT + MZ) - MZ * MT
        pool.submit(EquationThreads.secondEquationThread(latch, resultQueue)); // Submit second equation task to the pool

        new Thread(() -> {
            try {
                int count = 0;
                while (count < 2) {
                    double[][] result = resultQueue.take(); // Take the next result from the queue
                    String equationName = result.length == 1 ? "Y" : "MA";
                    lock.lock(); // Acquire the lock before modifying shared data
                    try {
                        Data.saveToFile(equationName, result);
                        Data.print(equationName, result);
                        count++;
                    } finally {
                        lock.unlock(); // Release the lock after modifying shared data
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            latch.countDown(); // Signal that this thread has finished
        }).start();

        pool.shutdown(); // Shutdown the pool

        try {
            latch.await(); // Wait for both equations to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Program finished");
        System.out.println("Total time: " + (endTime - startTime) + " ms");
    }


    private static void readData(int n) {
        try {
            MT = Data.readMatrixFromFile("MT", n, n);
            MZ = Data.readMatrixFromFile("MZ", n, n);
            D = Data.readMatrixFromFile("D", 1, n);
            B = Data.readMatrixFromFile("B", 1, n);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    private static void generateData(int n) {
        generateAndSaveToFile("MT", n, n);
        generateAndSaveToFile("MZ", n, n);
        generateAndSaveToFile("D", 1, n);
        generateAndSaveToFile("B", 1, n);
    }
}