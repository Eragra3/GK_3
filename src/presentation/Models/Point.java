package presentation.Models;

import Common.MatrixHelper;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

/**
 * Created by bider_000 on 15.11.2015.
 */
public class Point extends Shape {

    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void transform(double[][] transformationMatrix) {
        double[][] M = {
                {x},
                {y},
                {1}};
        double[][] result = MatrixHelper.multiply(transformationMatrix, M);

        //scale
        double s = result[2][0];

        x = result[0][0] / s;
        y = result[1][0] / s;
    }

    @Override
    public javafx.scene.shape.Shape toJavaFXShape() {
        javafx.scene.shape.Ellipse preview = new Ellipse();
        preview.setFill(Color.BLACK);
        preview.setRadiusX(scale / 2);
        preview.setRadiusY(scale / 2);
        preview.setCenterX(x * scale + xTransl);
        preview.setCenterY(-(y  * scale + yTransl));
        preview.setVisible(true);
        return preview;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
