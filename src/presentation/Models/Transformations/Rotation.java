package presentation.Models.Transformations;

import logic.TransformationMatrixToolbox;

/**
 * Created by bider_000 on 22.11.2015.
 */
public class Rotation extends Transformation {
    private double phi;

    public Rotation(double phi) {
        this.phi = phi;
    }

    @Override
    public double[][] addSelf(double[][] M) {
        return TransformationMatrixToolbox.rotate(M, phi);
    }

    @Override
    public String toString() {
        return "Rotation{" +
                "phi=" + phi +
                '}';
    }
}
