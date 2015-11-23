package presentation.Models;

import Common.MatrixHelper;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

/**
 * Created by bider_000 on 15.11.2015.
 */
public class Line extends Shape {

    double startX;
    double endX;

    double startY;
    double endY;

    public Line(double startX, double startY, double endX, double endY) {
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
    }

    @Override
    public void transform(double[][] transformationMatrix) {
        double[][] M1 = {
                {startX},
                {startY},
                {1}};
        double[][] result1 = MatrixHelper.multiply(transformationMatrix, M1);

        //scale
        double s = result1[2][0];

        startX = result1[0][0] / s;
        startY = result1[1][0] / s;

        double[][] M2 = {{endX}, {endY}, {1}};
        double[][] result2 = MatrixHelper.multiply(transformationMatrix, M2);
        s = result1[2][0];

        endX = result2[0][0] / s;
        endY = result2[1][0] / s;
    }

    @Override
    public javafx.scene.shape.Shape toJavaFXShape() {
        javafx.scene.shape.Polyline preview = new Polyline();

        ObservableList<Double> previewPoints = preview.getPoints();
        previewPoints.add(startX * scale + xTransl);
        previewPoints.add(-(startY * scale + yTransl));

        previewPoints.add(endX * scale + xTransl);
        previewPoints.add(-(endY * scale + yTransl));

        preview.setFill(Color.BLACK);
        preview.setVisible(true);
        return preview;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }
}
