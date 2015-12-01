package presentation.Models.Transformations;

import logic.TransformationMatrixToolbox;

/**
 * Created by bider_000 on 23.11.2015.
 */
public class ShearingY extends Transformation {
    private double y;

    public ShearingY(double y) {
        this.y = y;
    }

    @Override
    public double[][] addSelf(double[][] M) {
        return TransformationMatrixToolbox.shearX(M, y);
    }

    @Override
    public String toString() {
        return "ShearingY{" +
                "y=" + y +
                '}';
    }
}