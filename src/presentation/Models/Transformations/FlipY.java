package presentation.Models.Transformations;

import logic.TransformationMatrixToolbox;

/**
 * Created by bider_000 on 23.11.2015.
 */
public class FlipY extends Transformation{

    @Override
    public double[][] addSelf(double[][] M) {
        return TransformationMatrixToolbox.symmetryY(M);
    }

    @Override
    public String toString() {
        return "FlipY{}";
    }
}
