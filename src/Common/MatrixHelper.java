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
}
