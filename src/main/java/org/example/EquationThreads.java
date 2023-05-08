package org.example;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import static org.example.Main.*;

public class EquationThreads {

    public static Callable<double[][]> firstEquationThread(CountDownLatch latch, CyclicBarrier barrier) {
        return () -> {
            Thread.currentThread().setName("First equation thread");
            System.out.println(Thread.currentThread().getName() + " was started");

            try {
                barrier.await(); // Wait for both equations to reach the barrier
            } catch (Exception e) {
                e.printStackTrace();
            }

            long startTime = System.currentTimeMillis();

            // Y = D * MT + max(B) * D

            // D * MT
            double[][] matrixMult1 = Operations.multiply(D, MT);

            // max(B) * D
            double[][] matrixMult2 = Operations.multiply(D, Operations.max(B));

            // Y = D * MT + max(B) * D
            double[][] Y = Operations.sum(matrixMult1, matrixMult2);

            lock.lock(); // Acquire the lock before modifying shared data
            try {
//                Main.Y = Y;
                Data.saveToFile("Y", Y);
                Data.print("Y", Y);
            } finally {
                lock.unlock(); // Release the lock after modifying shared data
            }

            long endTime = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + " was finished in " + (endTime - startTime) + " ms");

            latch.countDown(); // Signal that this equation has finished
            return Y; // Return the result
        };
    }

    public static Callable<double[][]> secondEquationThread(CountDownLatch latch, CyclicBarrier barrier) {
        return () -> {
            Thread.currentThread().setName("Second equation thread");
            System.out.println(Thread.currentThread().getName() + " was started");

            try {
                barrier.await(); // Wait for both equations to reach the barrier
            } catch (Exception e) {
                e.printStackTrace();
            }

            long startTime = System.currentTimeMillis();

            // MA = MT * (MT + MZ) - MZ * MT

            // MT * (MT + MZ)
            double[][] matrixSum = Operations.multiply(MT, Operations.sum(MT, MZ));

            // MZ * MT
            double[][] matrixMult = Operations.multiply(MZ, MT);

            // MA = MT * (MT + MZ) - MZ * MT
            double[][] MA = Operations.subtract(matrixSum, matrixMult);

            lock.lock(); // Acquire the lock before modifying shared data
            try {
//                Main.MA = MA;
                Data.saveToFile("MA", MA);
                Data.print("MA", MA);
            } finally {
                lock.unlock(); // Release the lock after modifying shared data
            }

            long endTime = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + " was finished in " + (endTime - startTime) + " ms");

            latch.countDown(); // Signal that this equation has finished
            return MA; // Return the result
        };
    }
}
