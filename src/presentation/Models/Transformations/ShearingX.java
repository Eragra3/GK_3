package presentation.Models.Transformations;

import logic.TransformationMatrixToolbox;

/**
 * Created by bider_000 on 23.11.2015.
 */
public class ShearingX extends Transformation {
    private double x;

    public ShearingX(double x) {
        this.x = x;
    }

    @Override
    public double[][] addSelf(double[][] M) {
        return TransformationMatrixToolbox.shearX(M, x);
    }

    @Override
    public String toString() {
        return "ShearingX{" +
                "x=" + x +
                '}';
    }
}