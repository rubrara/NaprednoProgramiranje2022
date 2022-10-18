package mk.ukim.finki.av1;

class Matrix {
    public static double sum(double[][] a) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                sum += a[i][j];
            }
        }
        return sum;
    }

    public static double average(double[][] b) {
        int count = b.length * b[0].length;

        return sum(b) / count;
    }
}

public class MatrixTest {

    public static void main(String[] args) {
        int m = 2;

        double[][] matrix = new double[][]{{1.4, 2.5}, {5.2, 2.1}};

        System.out.println(Matrix.sum(matrix));
        System.out.println(Matrix.average(matrix));
    }
}
