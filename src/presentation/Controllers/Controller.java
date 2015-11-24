package presentation.Controllers;

import Common.Helpers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import logic.BackgroundDrawer;
import logic.IBackgroundsDrawer;
import logic.RasterImageWrapper;
import logic.TransformationMatrixToolbox;
import org.fxmisc.easybind.EasyBind;
import org.fxmisc.easybind.monadic.MonadicBinding;
import presentation.Models.*;
import presentation.Models.Transformations.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private final FileChooser fileChooser = new FileChooser();

    private static final double MAX_SCALE = 10.0d;
    private static final double MIN_SCALE = 0.1d;
    private static final double PANNING_SPEED = 0.3;

    @FXML
    ListView listView;
    @FXML
    TabPane tabPane;
    SingleSelectionModel<Tab> tabPaneSelectionModel;
    //Raster
    RasterImageWrapper rasterImage;

    @FXML
    ScrollPane scrollPaneR;

    @FXML
    ImageView canvasR;

    @FXML
    Group groupR;

    @FXML
    ImageView imageViewR;
    //Vector
    @FXML
    ScrollPane scrollPaneV;

    @FXML
    ImageView canvasV;

    @FXML
    Group groupV;

    @FXML
    Pane imageViewV;

    //Transformations
    @FXML
    TextField transformX;
    @FXML
    TextField transformY;
    @FXML
    TextField transformPhi;
    @FXML
    CheckBox angleCheckbox;

    //Logic
    IBackgroundsDrawer bgDrawer = new BackgroundDrawer();

    VectorImage vectorImage;
    ArrayList<javafx.scene.shape.Shape> vectorImagePreview = new ArrayList<>();

    @FXML
    ObservableList<Transformation> transformationsQueue = FXCollections.observableArrayList();

    public void initialize() {
        //listView
        listView.setItems(transformationsQueue);
        listView.setOnKeyReleased(event -> {
            final Object selectedItem = listView.getSelectionModel().getSelectedItem();
            if (event.getCode() == KeyCode.DELETE) {
                transformationsQueue.remove(selectedItem);
            }
        });
        //

        //scaling/scrolling
        Scale scaleR = new Scale();
        //binding properties
        MonadicBinding<Double> maxWidthR = EasyBind.map(imageViewR.boundsInParentProperty(), Bounds::getWidth);
        MonadicBinding<Double> maxHeightR = EasyBind.map(imageViewR.boundsInParentProperty(), Bounds::getHeight);
        MonadicBinding<Number> imageWidthR = EasyBind.select(imageViewR.imageProperty()).selectObject(Image::widthProperty);
        MonadicBinding<Number> imageHeightR = EasyBind.select(imageViewR.imageProperty()).selectObject(Image::heightProperty);

        scaleR.xProperty().bind(Helpers.divide(maxWidthR, imageWidthR));
        scaleR.yProperty().bind(Helpers.divide(maxHeightR, imageHeightR));
        canvasR.getTransforms().add(scaleR);

        groupR.addEventFilter(ScrollEvent.SCROLL, this::handleScrollR);
        //
        double maxW = scrollPaneV.getWidth();
        double maxH = scrollPaneV.getHeight();
        imageViewV.setMinWidth(maxW);
        imageViewV.setMaxWidth(maxW);
        imageViewV.setPrefWidth(maxW);
        imageViewV.setMinHeight(maxH);
        imageViewV.setMaxHeight(maxH);
        imageViewV.setPrefHeight(maxH);

        groupV.addEventFilter(ScrollEvent.SCROLL, this::handleScrollV);
        //
        tabPaneSelectionModel = tabPane.getSelectionModel();
        rasterImage = new RasterImageWrapper();
    }


    @FXML
    public void loadImageR() {
        Scene scene = imageViewR.getScene();

        File file = fileChooser.showOpenDialog(scene.getWindow());
        if (file != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                imageViewR.setImage(image);
                rasterImage.setImage(bufferedImage);

                BackgroundImage bg = bgDrawer.draw(scrollPaneR.getWidth(), scrollPaneR.getHeight());
                canvasR.setImage(bg.getImage());

                centerImageView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void loadImageV() {
        vectorImage = new VectorImage();
        Shape.xTransl = imageViewV.getWidth();
        Shape.yTransl = imageViewV.getHeight();

        Polygon polygon = new Polygon();
        polygon.addPoint(2, 1);
        polygon.addPoint(2, 4);
        polygon.addPoint(0, 6);
        polygon.addPoint(-2, 4);
        polygon.addPoint(-2, 1);
        polygon.addPoint(2, 1);
        vectorImage.addShape(polygon);

        Line l = new Line(-10, -19, 10, 21);
        vectorImage.addShape(l);

        Point p = new Point(0, 0);
        vectorImage.addShape(p);
//
//        p = new Point(4, 2);
//        vectorImage.addShape(p);
        Line line = new Line(-2, 4, 2, 4);
        vectorImage.addShape(line);

        BackgroundImage bg = bgDrawer.draw(scrollPaneV.getWidth(), scrollPaneV.getHeight());
        canvasV.setImage(bg.getImage());

        generatePreviewV();
    }

    private void generatePreviewV() {
        Bounds canvasBounds = canvasV.getBoundsInParent();
        double xCenter = canvasBounds.getWidth() / 2;
        double yCenter = canvasBounds.getHeight() / 2;

        List<Node> displayedShapes = groupV.getChildren();
        if (vectorImagePreview != null)
            displayedShapes.removeAll(vectorImagePreview);

        vectorImagePreview = vectorImage.generatePreview(xCenter, yCenter);
        for (javafx.scene.shape.Shape shape : vectorImagePreview) {
            displayedShapes.add(shape);
        }
    }

    private void generatePreviewR() {
        BufferedImage bufferedImage = rasterImage.getImage();
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        imageViewR.setImage(image);
        rasterImage.updateImage(bufferedImage);
        centerImageView();
    }

    public void startPanningR(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.MIDDLE) {
            scrollPaneR.setPannable(true);
        }
    }

    public void stopPanningR(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.MIDDLE) {
            scrollPaneR.setPannable(false);
        }
    }

    public void startPanningV(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.MIDDLE) {
            scrollPaneV.setPannable(true);
        }
    }

    public void stopPanningV(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.MIDDLE) {
            scrollPaneV.setPannable(false);
        }
    }

    private void centerImageView() {
        Bounds bounds = groupR.getBoundsInParent();
        double xCenter = bounds.getWidth() / 2;
        double yCenter = bounds.getHeight() / 2;

        Bounds imageViewBounds = imageViewR.getBoundsInParent();
        imageViewR.setTranslateX(xCenter - (imageViewBounds.getWidth() / 2)  + rasterImage.getTranslateX());
        imageViewR.setTranslateY(yCenter - (imageViewBounds.getHeight() / 2) + rasterImage.getTranslateY());

        Bounds canvasBounds = canvasR.getBoundsInParent();
        canvasR.setTranslateX(xCenter - (canvasBounds.getWidth() / 2));
        canvasR.setTranslateY(yCenter - (canvasBounds.getHeight() / 2));
    }

    public void handleScrollR(ScrollEvent event) {
        if (event.isControlDown()) {

            double oldHvalue = scrollPaneR.getHvalue();
            double oldVvalue = scrollPaneR.getVvalue();
            Bounds bounds = groupR.getBoundsInParent();
            double oldWidth = bounds.getWidth();
            double oldHeight = bounds.getHeight();

            double delta = 1.1;

            double scale = imageViewR.getScaleX();

            if (event.getDeltaY() < 0)
                scale /= delta;
            else
                scale *= delta;

            scale = Helpers.clamp(scale, MIN_SCALE, MAX_SCALE);

            imageViewR.getTransforms().add(Transform.scale(scale, scale));

            centerImageView();
            //center viewport
            double centerX = scrollPaneR.getWidth() / 2;
            double centerY = scrollPaneR.getHeight() / 2;

            double dx = event.getSceneX() - centerX;
            double dy = event.getSceneY() - centerY;

            scrollPaneR.setHvalue(oldHvalue + (dx / oldWidth * PANNING_SPEED));
            scrollPaneR.setVvalue(oldVvalue + (dy / oldHeight * PANNING_SPEED));
        }
    }

    public void handleScrollV(ScrollEvent event) {
        if (event.isControlDown()) {

            double oldHvalue = scrollPaneV.getHvalue();
            double oldVvalue = scrollPaneV.getVvalue();
            Bounds bounds = groupV.getBoundsInParent();
            double oldWidth = bounds.getWidth();
            double oldHeight = bounds.getHeight();

            double delta = 1.1;

            double scale = imageViewV.getScaleX();

            if (event.getDeltaY() < 0)
                scale /= delta;
            else
                scale *= delta;

            scale = Helpers.clamp(scale, MIN_SCALE, MAX_SCALE);

            imageViewV.getTransforms().add(Transform.scale(scale, scale));

            //center viewport
            double centerX = scrollPaneV.getWidth() / 2;
            double centerY = scrollPaneV.getHeight() / 2;

            double dx = event.getSceneX() - centerX;
            double dy = event.getSceneY() - centerY;

            scrollPaneV.setHvalue(oldHvalue + (dx / oldWidth * PANNING_SPEED));
            scrollPaneV.setVvalue(oldVvalue + (dy / oldHeight * PANNING_SPEED));
        }
    }


    @FXML
    public void centerR() {
        if (imageViewR.getImage() != null) {
            centerImageView();
            scrollPaneR.setHvalue(0.5);
            scrollPaneR.setVvalue(0.5);
        }
    }

    @FXML
    public void centerV() {
        if (!vectorImagePreview.isEmpty()) {
            scrollPaneV.setHvalue(0.5);
            scrollPaneV.setVvalue(0.5);
        }
    }

    /*
            ----------------------- O P E R A T I O N S -----------------------
     */

    @FXML
    public void transform() {
        double[][] M = TransformationMatrixToolbox.getEmpty();

        for (Transformation operation : transformationsQueue) {
            M = operation.addSelf(M);
        }

        if (tabPaneSelectionModel.getSelectedIndex() == 0) {
            rasterImage.transform(M);
            generatePreviewR();
        } else {
            vectorImage.transform(M);
            generatePreviewV();
        }
    }

    @FXML
    public void addTranslation() {
        try {
            double x = parseDouble(transformX.getText());
            double y = parseDouble(transformY.getText());

            transformationsQueue.add(new Translation(x, y));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Parameters are invalid", ButtonType.OK);
            alert.show();
        }
    }

    @FXML
    public void addRotation() {
        try {
            double phi = parseDouble(transformPhi.getText());
            if (angleCheckbox.isSelected())
                phi = Math.toDegrees(Math.atan(phi));

            transformationsQueue.add(new Rotation(phi));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Parameters are invalid", ButtonType.OK);
            alert.show();
        }
    }

    @FXML
    public void addRotationOnPoint() {
        try {
            double phi = parseDouble(transformPhi.getText());
            double x = parseDouble(transformX.getText());
            double y = parseDouble(transformY.getText());
            if (angleCheckbox.isSelected())
                phi = Math.toDegrees(Math.atan(phi));

            transformationsQueue.add(new RotationOnPoint(phi, x, y));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Parameters are invalid", ButtonType.OK);
            alert.show();
        }
    }

    @FXML
    public void addScaling() {
        try {
            double x = parseDouble(transformX.getText());
            double y = parseDouble(transformY.getText());

            transformationsQueue.add(new Scaling(x, y));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Parameters are invalid", ButtonType.OK);
            alert.show();
        }
    }

    @FXML
    public void addFlipX() {
        transformationsQueue.add(new FlipX());
    }

    @FXML
    public void addFlipY() {
        transformationsQueue.add(new FlipY());
    }

    @FXML
    public void addShearingX() {
        try {
            double x = parseDouble(transformX.getText());

            transformationsQueue.add(new ShearingX(x));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Parameters are invalid", ButtonType.OK);
            alert.show();
        }
    }

    public double parseDouble(String s) throws Exception {
        if (s == null || s.isEmpty())
            return 0.0;
        return Double.parseDouble(s.replace(',', '.'));
    }
}
