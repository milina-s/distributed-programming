package org.example;

public class Operations {

    public static double[][] multiply(double[][] A, double[][] B) {
        int n = A.length;
        int m = B[0].length;
        int p = B.length;
        double[][] result = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                double sum = 0.0;
                double c = 0.0;
                for (int k = 0; k < p; k++) {
                    double y = A[i][k] * B[k][j] - c;
                    double t = sum + y;
                    c = (t - sum) - y;
                    sum = t;
                }
                result[i][j] = sum;
            }
        }
        return result;
    }


    public static double[][] multiply(double[][] A, double B) {
        int n = A.length;
        int m = A[0].length;
        double[][] result = new double[n][m];
        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, result[i], 0, m);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; ++j) {
                result[i][j] *= B;
            }
        }
        return result;
    }

    public static double[][] subtract(double[][] A, double[][] B) {
        int n = A.length;
        int m = A[0].length;
        double[][] result = new double[n][m];
        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, result[i], 0, m);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; ++j) {
                result[i][j] -= B[i][j];
            }
        }
        return result;
    }

    public static double[][] sum(double[][] A, double[][] B) {
        int n = A.length;
        int m = A[0].length;
        double[][] result = new double[n][m];
        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, result[i], 0, m);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; ++j) {
                result[i][j] += B[i][j];
            }
        }
        return result;
    }

    public static double max(double[][] A) {
        double max = A[0][0];
        for (double[] doubles : A) {
            for (int j = 0; j < A[0].length; j++) {
                if (doubles[j] > max) {
                    max = doubles[j];
                }
            }
        }
        return max;
    }

}
