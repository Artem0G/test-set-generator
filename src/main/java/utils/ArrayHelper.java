package utils;

public class ArrayHelper {
    public static Object[][] transposeMatrix(Object[][] matrix) {
        Object[][] matrixT = new Object[matrix[0].length][matrix.length];
        return transposeMatrix(matrix, matrixT);
    }

    public static Object[][] transposeMatrix(Object[][] matrix, Object[][] matrixT) {
        return transposeMatrix(matrix, matrixT, 0);
    }

    public static Object[][] transposeMatrix(Object[][] matrix, Object[][] matrixT, int startIndex) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrixT[startIndex + j][i] = matrix[i][j];
            }
        }
        return matrixT;
    }
}
