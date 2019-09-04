package algorithm.dl;

import java.util.*;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

/**
 * find the best way to calculate multiple matrix
 *
 * @author fengcaiwen
 * @since 9/2/2019
 */
public class MultipleMatrixTest {
    /*
     * find the order and calculate
     *
     * assume you have know knowledge about matrix, otherwise, please google/bing
     *
     * assume:
     * m[i, j] = k, from Ai to Aj, k is best cut point, it will cut off two parts, Ai A(i+1) ... Ak, A(k+1) A(K+2)... Aj
     * s[i, j] = cost, the step of Ai * A(i+1) * ... * Aj = cost
     *
     * Ai Ai+1 ... Aj
     * assume Ak and A(k+1) is the cut point
     *
     * so:
     * s(i, k)+s(k+1, j) < s(i,j)
     * if i = j, then s(i, j)=0
     * to m , j>i make sense
     *
     */
    public static Matrix test(ArrayList<Matrix> matrices) {

        // init two matrix, fill full with Integer.MAX_VALUE
        Matrix m = new Matrix(matrices.size(), matrices.size());
        Matrix s = new Matrix(matrices.size(), matrices.size());
        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.columns; j++) {
                m.set(i + 1, j + 1, Integer.MAX_VALUE);
                s.set(i + 1, j + 1, Integer.MAX_VALUE);
            }
        }

        for (int i = 1; i < matrices.size() + 1; i++) {
            for (int j = 1; j < matrices.size() + 1; j++) {
                if (j < i) continue;
                for (int k = i; k <= j; k++) {
                    int partA = 0;
                    int partB = 0;
                    Matrix c = matrices.get(i - 1);
                    for (int l = i + 1; l <= k; l++) {
                        Matrix a = matrices.get(l - 1);
                        partA += c.rows * c.columns * a.columns;
                        c = new Matrix(c.rows, a.columns);
                    }

                    Matrix d = matrices.get(k - 1);
                    for (int l = k + 1 + 1; l <= j; l++) {
                        Matrix b = matrices.get(l - 1);
                        partB += d.rows * d.columns * b.columns;
                        d = new Matrix(d.rows, b.columns);
                    }

                    int increment = c.rows * c.columns * d.columns;

                    // if k is best cut point, it should: s(i, k) + s(k+1, j) < s(i, j), otherwise, do nothing
                    if (partA + partB + increment < s.get(i, j)) {
                        s.set(i, j, partA + partB + increment);
                        m.set(i, j, k);
                    }
                }
            }
        }
//        System.out.println(m);
//        System.out.println(s);
        return m;
    }

    /*
     * excellent, use system stack to calculate
     */
    public static Matrix calculate(ArrayList<Matrix> matrices, Matrix m, int from, int to) {
        if (from + 1 == to) {
            return matrices.get(from - 1).multiply(matrices.get(to - 1));
        } else if (from == to) {
            return matrices.get(from - 1);
        }

        int i = m.get(from, to);
        Matrix a = calculate(matrices, m, from, i);
        Matrix b = calculate(matrices, m, i + 1, to);
        return a.multiply(b);
    }


    /*
     *
     * A(5x4) * A(4x7) = A(5x7)
     *
     */
    public static Matrix simpleMultiply(Matrix a, Matrix b) {
        assertEquals(a.columns, b.rows);
        Matrix c = new Matrix(a.rows, b.columns);
        for (int i = 1; i < a.rows + 1; i++) {
            for (int j = 1; j < b.columns + 1; j++) {
                int value = 0;
                for (int k = 1; k < a.columns + 1; k++)
                    value += a.get(i, k) * b.get(k, j);
                c.set(i, j, value);
            }
        }
        return c;
    }

    public static class Matrix {
        // multiple matrix actually can express as a array
        private int[] value;
        // the rows of matrix
        private int rows;
        // the columns of matrix
        private int columns;

        public Matrix(int[] ints, int rows, int columns) {
            assertEquals(ints.length, rows * columns);
            this.value = ints;
            this.rows = rows;
            this.columns = columns;
        }

        public Matrix(int rows, int columns) {
            this.value = new int[rows * columns];
            this.rows = rows;
            this.columns = columns;
        }

        public int get(int x, int y) {
            return this.value[(x - 1) * columns + (y - 1)];
        }

        public void set(int x, int y, int value) {
            this.value[(x - 1) * columns + (y - 1)] = value;
        }

        public int getRows() {
            return rows;
        }

        public int getColumns() {
            return columns;
        }

        public Matrix multiply(Matrix b) {
            return simpleMultiply(this, b);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < rows + 1; i++) {
                for (int j = 1; j < columns + 1; j++) {
                    sb.append(String.format("(%s, %s) = %s\n", i, j, get(i, j)));
                }
            }
            return sb.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Matrix matrix = (Matrix) o;
            return rows == matrix.rows &&
                    columns == matrix.columns &&
                    Arrays.equals(value, matrix.value);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(rows, columns);
            result = 31 * result + Arrays.hashCode(value);
            return result;
        }
    }

    public static void main(String[] args) {
        Matrix a = new Matrix(IntStream.range(0, 30).toArray(), 6, 5);
        Matrix b = new Matrix(IntStream.range(0, 20).toArray(), 5, 4);
        Matrix c = new Matrix(IntStream.range(0, 12).toArray(), 4, 3);
        Matrix d = new Matrix(IntStream.range(0, 6).toArray(), 3, 2);
        ArrayList<Matrix> matrices = new ArrayList<>(Arrays.asList(a, b, c, d));
        Matrix m = test(matrices);
//        for (int i = 0; i < matrices.size(); i++) {
//            System.out.println(matrices.get(i));
//        }
        Matrix result1 = a.multiply(b).multiply(c).multiply(d);
        System.out.println();
        Matrix result2 = calculate(matrices, m, 1, m.rows);
        System.out.println(result1);
        System.out.println(result2);
        System.out.println(result1.equals(result2));
    }

}
