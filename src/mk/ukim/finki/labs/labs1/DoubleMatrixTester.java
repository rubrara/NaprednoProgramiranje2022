package mk.ukim.finki.labs.labs1;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

class InsufficientElementsException extends Exception {
    public InsufficientElementsException() {
        super("Insufficient number of elements");
    }
}

class InvalidRowNumberException extends Exception {
    public InvalidRowNumberException() {
        super("Invalid row number");
    }
}

class InvalidColumnNumberException extends Exception {
    public InvalidColumnNumberException() {
        super("Invalid column number");
    }
}

class DoubleMatrix {

    private final double a[][];


    // m и n се димензии на матрицата. Ако нема доволно елементи се фрла исклучок.
    public DoubleMatrix(double[] a, int m, int n) throws InsufficientElementsException {
        if (a.length < m * n) throw new InsufficientElementsException();

        int k = n * m;
        double[] b = Arrays.copyOfRange(a, a.length - k, a.length);

        this.a = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                this.a[i][j] = b[i * n + j];
            }
        }
    }

    public String getDimensions() {
        return String.format("[%d x %d]", a.length, a[0].length);
    }

    public int rows() {
        return a.length;
    }

    public int columns() {
        return a[0].length;
    }

    // Го враќа максималниот елемент во дадениот ред, доколку вредноста е ред кој не постои да се фрли исклучок
    // (row има вредност [1, m])
    public double maxElementAtRow(int row) throws InvalidRowNumberException {

        if (row > a.length || row < 1) throw new InvalidRowNumberException();

        double max = a[row-1][0];

        for (int i = 0; i < a[0].length; i++) {
            if (max < a[row-1][i]) max = a[row-1][i];
        }

        return max;
    }

    // Го враќа максималниот елемент во дадениот ред, доколку вредноста е ред кој не постои да се фрли исклучок
    // (column има вредност [1, m])
    public double maxElementAtColumn(int column) throws InvalidColumnNumberException {

        if (column > a[0].length || column < 1) throw new InvalidColumnNumberException();

        double max = a[0][column-1];

        for (int i = 0; i < a.length; i++) {
            if (max < a[i][column - 1]) max = a[i][column - 1];
        }

        return max;
    }

    double sum() {

        double sum = 0;

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                sum += a[i][j];
            }
        }
        return sum;
    }

    public double[] toSortedArray() {
        double[] result = new double[a.length * a[0].length];

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                result[i * a[0].length + j] = a[i][j];
            }
        }

        for (int i = 0; i < result.length; i++){
            result[i] *= -1;
        }

        Arrays.sort(result);

        for (int i = 0; i < result.length; i++){
            result[i] *= -1;
        }

        return result;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#0.00");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length - 1; j++) {
                sb.append(df.format(a[i][j])).append("\t");
            }
            sb.append(df.format(a[i][a[0].length - 1]));
            if (i != a.length - 1)
                sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleMatrix that = (DoubleMatrix) o;
        return Arrays.deepEquals(a, that.a);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(a);
    }
}

class MatrixReader {


    public static DoubleMatrix read(InputStream input) throws IOException, InsufficientElementsException {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//
//        String line = reader.readLine();
//
//        String[] parts = line.split("\\s+");
//
//        int m = Integer.parseInt(parts[0]);
//        int n = Integer.parseInt(parts[1]);
//
//        double a[] = new double[m * n];
//
//        StringBuilder sb = new StringBuilder();
//
//        while (reader.readLine() != null) {
//            if (!reader.readLine().isEmpty()) {
//                String rowLine = reader.readLine();
//                sb.append(rowLine);
//            }
//        }
//
//        String[] stringValues = sb.toString().split("\\s+");
//
//        for (int i = 0; i < m * n; i++) {
//            a[i] = Double.parseDouble(stringValues[i]);
//        }
//
//        return new DoubleMatrix(a, m, n);


        Scanner sc = new Scanner(input);
        int m = sc.nextInt();
        int n = sc.nextInt();
        double[] arr = new double[m * n];
        /* for (int i=0; i<m; i++){
            arr[i] = sc.nextDouble();
        }*/
        int i = 0;
        while (sc.hasNextDouble()) {
            arr[i++] = sc.nextDouble();
        }
        return new DoubleMatrix(arr, m, n);
    }
}

public class DoubleMatrixTester {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        DoubleMatrix fm = null;

        double[] info = null;

        DecimalFormat format = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            String operation = scanner.next();

            switch (operation) {
                case "READ": {
                    int N = scanner.nextInt();
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    double[] f = new double[N];

                    for (int i = 0; i < f.length; i++)
                        f[i] = scanner.nextDouble();

                    try {
                        fm = new DoubleMatrix(f, R, C);
                        info = Arrays.copyOf(f, f.length);

                    } catch (InsufficientElementsException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }

                    break;
                }

                case "INPUT_TEST": {
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    StringBuilder sb = new StringBuilder();

                    sb.append(R + " " + C + "\n");

                    scanner.nextLine();

                    for (int i = 0; i < R; i++)
                        sb.append(scanner.nextLine() + "\n");

                    fm = MatrixReader.read(new ByteArrayInputStream(sb
                            .toString().getBytes()));

                    info = new double[R * C];
                    Scanner tempScanner = new Scanner(new ByteArrayInputStream(sb
                            .toString().getBytes()));
                    tempScanner.nextDouble();
                    tempScanner.nextDouble();
                    for (int z = 0; z < R * C; z++) {
                        info[z] = tempScanner.nextDouble();
                    }

                    tempScanner.close();

                    break;
                }

                case "PRINT": {
                    System.out.println(fm.toString());
                    break;
                }

                case "DIMENSION": {
                    System.out.println("Dimensions: " + fm.getDimensions());
                    break;
                }

                case "COUNT_ROWS": {
                    System.out.println("Rows: " + fm.rows());
                    break;
                }

                case "COUNT_COLUMNS": {
                    System.out.println("Columns: " + fm.columns());
                    break;
                }

                case "MAX_IN_ROW": {
                    int row = scanner.nextInt();
                    try {
                        System.out.println("Max in row: "
                                + format.format(fm.maxElementAtRow(row)));
                    } catch (InvalidRowNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "MAX_IN_COLUMN": {
                    int col = scanner.nextInt();
                    try {
                        System.out.println("Max in column: "
                                + format.format(fm.maxElementAtColumn(col)));
                    } catch (InvalidColumnNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "SUM": {
                    System.out.println("Sum: " + format.format(fm.sum()));
                    break;
                }

                case "CHECK_EQUALS": {
                    int val = scanner.nextInt();

                    int maxOps = val % 7;

                    for (int z = 0; z < maxOps; z++) {
                        double work[] = Arrays.copyOf(info, info.length);

                        int e1 = (31 * z + 7 * val + 3 * maxOps) % info.length;
                        int e2 = (17 * z + 3 * val + 7 * maxOps) % info.length;

                        if (e1 > e2) {
                            double temp = work[e1];
                            work[e1] = work[e2];
                            work[e2] = temp;
                        }

                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(work, fm.rows(),
                                fm.columns());
                        System.out
                                .println("Equals check 1: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    if (maxOps % 2 == 0) {
                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(new double[]{3.0, 5.0,
                                7.5}, 1, 1);

                        System.out
                                .println("Equals check 2: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    break;
                }

                case "SORTED_ARRAY": {
                    double[] arr = fm.toSortedArray();

                    String arrayString = "[";

                    if (arr.length > 0)
                        arrayString += format.format(arr[0]) + "";

                    for (int i = 1; i < arr.length; i++)
                        arrayString += ", " + format.format(arr[i]);

                    arrayString += "]";

                    System.out.println("Sorted array: " + arrayString);
                    break;
                }

            }

        }

        scanner.close();
    }
}

