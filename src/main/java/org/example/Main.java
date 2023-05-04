package org.example;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import static org.example.Data.*;

/**
 * Y = D * MT + max(B) * D
 * MA = MT * (MT + MZ) - MZ * MT
 * </p>
 * Matrices: MT, MZ - square matrices [n x n]
 * Vectors: D, B - column vectors [1 x n]
 */

public class Main {

    public static double[][] MT, MZ, MA;
    public static double[][] D, B, Y;

    public static void main(String[] args) {

        int n = 200;
//        System.out.println("Enter n: ");
//        Scanner scanner = new Scanner(System.in);
//        n = scanner.nextInt();

//        generateData(n);
        readData(n);

        CountDownLatch latch = new CountDownLatch(2); // To wait for both equations to finish
        CyclicBarrier barrier = new CyclicBarrier(2); // To synchronize the start of both equations

        // Y = D * MT + max(B) * D
        Thread thread1 = new Thread(EquationThreads.firstEquationThread(latch, barrier));
        // MA = MT * (MT + MZ) - MZ * MT
        Thread thread2 = new Thread(EquationThreads.secondEquationThread(latch, barrier));

        thread1.start();
        thread2.start();

        try {
            latch.await(); // Wait for both equations to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Program finished");
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