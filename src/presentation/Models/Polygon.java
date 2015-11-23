package presentation.Models;

import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polyline;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by bider_000 on 15.11.2015.
 */
public class Polygon extends Shape {

    private ArrayList<Point> points;

    public Polygon() {
        points = new ArrayList<Point>(12);
    }

    public void addPoint(double x, double y) {
        points.add(new Point(x, y));
    }

    public void unshift(double x, double y) {
        points.add(0, new Point(x, y));
    }

    @Override
    public void transform(double[][] transformationMatrix) {
        for (Point point : points) {
            point.transform(transformationMatrix);
        }
    }

    @Override
    public javafx.scene.shape.Shape toJavaFXShape() {
        javafx.scene.shape.Polyline preview = new Polyline();

        ObservableList<Double> previewPoints = preview.getPoints();
        for (Point point: points) {
            previewPoints.add(point.getX() * scale + xTransl);
            previewPoints.add(-(point.getY() * scale + yTransl));
        }

        preview.setFill(Color.valueOf("0xff000000"));
        preview.setVisible(true);
        return preview;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }
}
