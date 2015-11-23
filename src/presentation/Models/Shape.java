package presentation.Models;

/**
 * Created by bider_000 on 15.11.2015.
 */
public abstract class Shape {
    public static double scale = 10;
    public static double xTransl = 0;
    public static double yTransl = 0;

    public abstract void transform(double[][] transformationMatrix);

    public abstract javafx.scene.shape.Shape toJavaFXShape();
}
