package presentation.Models.Transformations;

import logic.TransformationMatrixToolbox;

/**
 * Created by bider_000 on 23.11.2015.
 */
public class FlipX extends Transformation{

    @Override
    public double[][] addSelf(double[][] M) {
        return TransformationMatrixToolbox.symmetryX(M);
    }

    @Override
    public String toString() {
        return "FlipX{}";
    }
}
