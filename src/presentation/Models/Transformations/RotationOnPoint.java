package presentation.Models.Transformations;

import logic.TransformationMatrixToolbox;

/**
 * Created by bider_000 on 23.11.2015.
 */
public class RotationOnPoint extends Transformation {
    private double x;
    private double y;
    private double phi;

    public RotationOnPoint(double phi, double x, double y) {
        this.phi = phi;
        this.x = x;
        this.y = y;
    }

    @Override
    public double[][] addSelf(double[][] M) {
        M = TransformationMatrixToolbox.translate(M, x, y);
        M = TransformationMatrixToolbox.rotate(M, phi);
        M = TransformationMatrixToolbox.translate(M, -x, -y);

        return M;
    }

    @Override
    public String toString() {
        return "RotationOnPoint{" +
                "x=" + x +
                ", y=" + y +
                ", phi=" + phi +
                '}';
    }
}
