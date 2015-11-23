package presentation.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bider_000 on 15.11.2015.
 */
public class VectorImage {

    private List<Shape> shapes;

    public VectorImage() {
        shapes = new ArrayList<Shape>();
    }

    public void addShape(Shape s) {
        shapes.add(s);
    }

    public void removeShape(Shape s) {
        shapes.remove(s);
    }

    public void transform(double[][] transformationMatrix) {
        for (Shape s : shapes) {
            s.transform(transformationMatrix);
        }
    }

    public ArrayList<javafx.scene.shape.Shape> generatePreview(double x, double y) {
        ArrayList<javafx.scene.shape.Shape> preview = new ArrayList<>(shapes.size());
        for (Shape shape : getShapes()) {
            javafx.scene.shape.Shape s = shape.toJavaFXShape();
            s.setTranslateX(x);
            s.setTranslateY(y);
            preview.add(s);
        }
        return preview;
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes;
    }
}
