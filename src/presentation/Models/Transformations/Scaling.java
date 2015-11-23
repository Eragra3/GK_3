package presentation.Models.Transformations;

import logic.TransformationMatrixToolbox;

/**
 * Created by bider_000 on 22.11.2015.
 */
public class Scaling extends Transformation {
    private double x;
    private double y;

    public Scaling(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double[][] addSelf(double[][] M) {
        return TransformationMatrixToolbox.scale(M, x, y);
    }

    @Override
    public String toString() {
        return "Scaling{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
