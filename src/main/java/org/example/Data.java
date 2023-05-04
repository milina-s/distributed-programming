package org.example;

import java.io.*;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Data {

    private static final Random random = new Random();

    public static void generateAndSaveToFile(String fileName, int n, int m) {
        double[][] values = new double[n][m];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; j++) {
                values[i][j] = 100 * random.nextDouble();
            }
        }
        saveToFile(fileName, values);

    }

    public static void saveToFile(String fileName, double[][] values) {
        String file = "./data/" + fileName + ".txt";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (double[] value : values) {
                String sb = IntStream.range(0, values[0].length)
                        .mapToObj(j -> value[j] + " ")
                        .collect(Collectors.joining());
                writer.write(sb + "\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static double[][] readMatrixFromFile(String fileName, int n, int m) throws FileNotFoundException {
        String path = "./data/" + fileName + ".txt";
        File file = new File(path);
        double[][] result = new double[n][m];
        Scanner scanner = new Scanner(file);
        int i = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] numbers = line.trim().split(" ");
            for (int j = 0; j < numbers.length; ++j) {
                result[i][j] = Double.parseDouble(numbers[j]);
            }
            i++;
        }
        return result;
    }

    public synchronized static void print(String name, double[][] matrix) {
        System.out.println(name + ":");
        for (double[] doubles : matrix) {
            for (double aDouble : doubles) {
                System.out.print(aDouble + " ");
            }
            System.out.println();
        }
    }
}
