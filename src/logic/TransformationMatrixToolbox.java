package logic;

import Common.MatrixHelper;

/**
 * Created by bider_000 on 15.11.2015.
 */
public interface TransformationMatrixToolbox {
    static double[][] getEmpty() {
        double[][] M = {
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}};
        return M;
    }

    static double[][] translate(double[][] M, double x, double y) {
        double[][] T = {
                {1, 0, x},
                {0, 1, y},
                {0, 0, 1}};

        return MatrixHelper.multiply(M, T);
    }

    static double[][] scale(double[][] M, double x, double y) {
        double[][] T = {
                {x, 0, 0},
                {0, y, 0},
                {0, 0, 1}};

        return MatrixHelper.multiply(M, T);
    }

    static double[][] rotate(double[][] M, double phi) {
        phi = Math.toRadians(phi);
        double[][] T = {
                {Math.cos(phi), -Math.sin(phi), 0},
                {Math.sin(phi), Math.cos(phi), 0},
                {0, 0, 1}};

        return MatrixHelper.multiply(M, T);
    }

    static double[][] shearX(double[][] M, double x) {
        double[][] T = {
                {1, x, 0},
                {0, 1, 0},
                {0, 0, 1}};

        return MatrixHelper.multiply(M, T);
    }

    static double[][] shearY(double[][] M, double y) {
        double[][] T = {
                {1, 0, 0},
                {y, 1, 0},
                {0, 0, 1}};

        return MatrixHelper.multiply(M, T);
    }

    static double[][] symmetryX(double[][] M) {
        double[][] T = {
                {1, 0, 0},
                {0, -1, 0},
                {0, 0, 1}};

        return MatrixHelper.multiply(M, T);
    }

    static double[][] symmetryY(double[][] M) {
        double[][] T = {
                {-1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}};

        return MatrixHelper.multiply(M, T);
    }
}
