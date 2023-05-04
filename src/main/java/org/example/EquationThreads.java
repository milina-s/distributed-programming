package org.example;

import static org.example.Main.*;

public class EquationThreads {

    public static Runnable firstEquationThread() {
        return () -> {
            Thread.currentThread().setName("First equation thread");
            long startTime = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + " was started");

            // Y = D * MT + max(B) * D

            // D * MT
            double[][] matrixMult1 = Operations.multiply(D, MT);

            // max(B) * D
            double[][] matrixMult2 = Operations.multiply(D, Operations.max(B));

            // Y = D * MT + max(B) * D
            double[][] Y = Operations.sum(matrixMult1, matrixMult2);

            Main.Y = Y;
            Data.saveToFile("Y", Y);
            Data.print("Y", Y);
            long endTime = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + " was finished in " + (endTime - startTime) + " ms");

        };
    }

    public static Runnable secondEquationThread() {
        return () -> {
            Thread.currentThread().setName("Second equation thread");
            long startTime = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + " was started");

            // MA = MT * (MT + MZ) - MZ * MT

            // MT * (MT + MZ)
            double[][] matrixSum = Operations.multiply(MT, Operations.sum(MT, MZ));

            // MZ * MT
            double[][] matrixMult = Operations.multiply(MZ, MT);

            // MA = MT * (MT + MZ) - MZ * MT
            double[][] MA = Operations.subtract(matrixSum, matrixMult);

            Main.MA = MA;
            Data.saveToFile("MA", MA);
            Data.print("MA", MA);
            long endTime = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + " was finished in " + (endTime - startTime) + " ms");

        };
    }
}
