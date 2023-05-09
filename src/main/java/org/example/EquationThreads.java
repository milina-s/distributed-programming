package org.example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RecursiveAction;

import static org.example.Main.*;

public class EquationThreads {

    public static RecursiveAction firstEquationThread(CountDownLatch latch, BlockingQueue<double[][]> resultQueue) {
        return new RecursiveAction() {
            @Override
            protected void compute() {
                Thread.currentThread().setName("First equation thread");
                System.out.println(Thread.currentThread().getName() + " was started");

                long startTime = System.currentTimeMillis();

                // Y = D * MT + max(B) * D

                // D * MT
                double[][] matrixMult1 = Operations.multiply(D, MT);

                // max(B) * D
                double[][] matrixMult2 = Operations.multiply(D, Operations.max(B));

                // Y = D * MT + max(B) * D
                double[][] Y = Operations.sum(matrixMult1, matrixMult2);

                long endTime = System.currentTimeMillis();
                System.out.println(Thread.currentThread().getName() + " was finished in " + (endTime - startTime) + " ms");

                try {
                    resultQueue.put(Y); // Add the result to the queue
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                latch.countDown(); // Signal that this equation has finished
            }
        };
    }

    public static RecursiveAction secondEquationThread(CountDownLatch latch, BlockingQueue<double[][]> resultQueue) {
        return new RecursiveAction() {
            @Override
            protected void compute() {
                Thread.currentThread().setName("Second equation thread");
                System.out.println(Thread.currentThread().getName() + " was started");

                long startTime = System.currentTimeMillis();

                // MA = MT * (MT + MZ) - MZ * MT

                // MT * (MT + MZ)
                double[][] matrixSum = Operations.multiply(MT, Operations.sum(MT, MZ));

                // MZ * MT
                double[][] matrixMult = Operations.multiply(MZ, MT);

                // MA = MT * (MT + MZ) - MZ * MT
                double[][] MA = Operations.subtract(matrixSum, matrixMult);

                long endTime = System.currentTimeMillis();
                System.out.println(Thread.currentThread().getName() + " was finished in " + (endTime - startTime) + " ms");

                try {
                    resultQueue.put(MA); // Add the result to the queue
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                latch.countDown(); // Signal that this equation has finished
            }
        };
    }
}