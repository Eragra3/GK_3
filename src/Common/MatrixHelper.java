package Common;

/**
 * Created by bider_000 on 15.11.2015.
 */
public interface MatrixHelper {

    static double[][] multiply(double[][] A, double[][] B) {
        if (A.length == 0) return new double[0][0];
        if (A[0].length != B.length) return null; //invalid dims

        int n = A[0].length;
        int m = A.length;
        int p = B[0].length;

        double ans[][] = new double[m][p];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < p; j++) {
                for (int k = 0; k < n; k++) {
                    ans[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return ans;
    }

    static double determinant(double[][] M) {
        double result = 0;
        if (M.length == 1) {
            result = M[0][0];
            return result;
        }
        if (M.length == 2) {
            result = M[0][0] * M[1][1] - M[0][1] * M[1][0];
            return result;
        }
        for (int i = 0; i < M[0].length; i++) {
            double temp[][] = new double[M.length - 1][M[0].length - 1];
            for (int c = 1; c < M.length; c++) {
                for (int a = 0; a < M[0].length; a++) {
                    if (a < i) {
                        temp[c - 1][a] = M[c][a];
                    } else if (a > i) {
                        temp[c - 1][a - 1] = M[c][a];
                    }
                }
            }
            result += M[0][i] * Math.pow(-1, i) * determinant(temp);
        }
        return result;
    }

    static double[][] invert(double[][] M) {
        double det = determinant(M);
        if (det == 0) return null;
        double n = 1 / det;
        if (n == 0) return null;

        double[][] res = {
                {M[1][1] * M[2][2] - M[1][2] * M[2][1], M[0][2] * M[2][1] - M[0][1] * M[2][2], M[0][1] * M[1][2] - M[0][2] * M[1][1]},
                {M[1][2] * M[2][0] - M[1][0] * M[2][2], M[0][0] * M[2][2] - M[0][2] * M[2][0], M[0][2] * M[1][0] - M[0][0] * M[1][2]},
                {M[1][0] * M[2][1] - M[1][1] * M[2][0], M[0][1] * M[2][0] - M[0][0] * M[2][1], M[0][0] * M[1][1] - M[0][1] * M[1][0]}
        };

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                res[i][j] = res[i][j] * n;
            }
        }

        return res;
    }
}
