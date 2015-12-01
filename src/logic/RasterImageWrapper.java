package logic;

import Common.MatrixHelper;
import javafx.geometry.Point2D;

import java.awt.image.BufferedImage;

/**
 * Created by bider_000 on 15.11.2015.
 */
public class RasterImageWrapper {

    private BufferedImage image;
    private BufferedImage modifiedImage;
    private double translateX;
    private double translateY;

    private double originTranslateX;
    private double originTranslateY;

    public RasterImageWrapper() {

    }

    public void transform(double[][] transformationMatrix) {
        //change translation
//        transformationMatrix[1][2] = -transformationMatrix[1][2];

        final double originWidth = image.getWidth();
        final double originHeight = image.getHeight();

//        final double scaleX = transformationMatrix[0][0];
//        final double scaleY = transformationMatrix[1][1];
//        final double translateX = transformationMatrix[0][2];
//        final double translateY = transformationMatrix[1][2];

        /*
                FIND IMAGE SIZE
         */
        Point2D p1 = multiply(0, 0, transformationMatrix);
        Point2D p2 = multiply(0, originHeight - 1, transformationMatrix);
        Point2D p3 = multiply(originWidth - 1, 0, transformationMatrix);
        Point2D p4 = multiply(originWidth - 1, originHeight - 1, transformationMatrix);
        //region finding out image size
        double maxH = p1.getY();
        if (p2.getY() > maxH)
            maxH = p2.getY();
        if (p3.getY() > maxH)
            maxH = p3.getY();
        if (p4.getY() > maxH)
            maxH = p4.getY();
        double minH = p1.getY();
        if (p2.getY() < minH)
            minH = p2.getY();
        if (p3.getY() < minH)
            minH = p3.getY();
        if (p4.getY() < minH)
            minH = p4.getY();
        double maxW = p1.getX();
        if (p2.getX() > maxW)
            maxW = p2.getX();
        if (p3.getX() > maxW)
            maxW = p3.getX();
        if (p4.getX() > maxW)
            maxW = p4.getX();
        double minW = p1.getX();
        if (p2.getX() < minW)
            minW = p2.getX();
        if (p3.getX() < minW)
            minW = p3.getX();
        if (p4.getX() < minW)
            minW = p4.getX();
        //endregion
        //region offset image
        double translateX = p1.getX();
        if (p2.getX() < translateX)
            translateX = p2.getX();
        if (p3.getX() < translateX)
            translateX = p3.getX();
        if (p4.getX() < translateX)
            translateX = p4.getX();
        double translateY = p1.getY();
        if (p2.getY() < translateY)
            translateY = p2.getY();
        if (p3.getY() < translateY)
            translateY = p3.getY();
        if (p4.getY() < translateY)
            translateY = p4.getY();
        //endregion

        int width = (int) Math.ceil(Math.abs(maxW - minW));
        int height = (int) Math.ceil(Math.abs(maxH - minH));

        BufferedImage newImage = new BufferedImage(
                width,
                height,
                BufferedImage.TYPE_INT_ARGB);
        //inverse matrix
        double[][] invTransfMatrix = MatrixHelper.invert(transformationMatrix);
        double[][] point = new double[3][1];
        double[][] result;

        double s, x, y, alpha, beta;

        int tl, tr, bl, br, cA, cB, cD;

        for (int i = 0; i < width - 1; i++) {
            for (int j = 0; j < height - 1; j++) {
                point[0][0] = i + translateX;
                point[1][0] = j + translateY;
                point[2][0] = 1;

                result = MatrixHelper.multiply(invTransfMatrix, point);

                //scale
                s = result[2][0];
                x = result[0][0] / s;
                y = result[1][0] / s;

                if (Math.ceil(y) > originHeight - 1 || Math.ceil(x) > originWidth - 1 || (int) y < 0 || (int) x < 0) {
//                    newImage.setRGB(i, j, 0xffff0000);
                    continue;
                }

                tl = image.getRGB((int) x, (int) y);
                tr = image.getRGB((int) Math.ceil(x), (int) y);
                bl = image.getRGB((int) x, (int) Math.ceil(y));
                br = image.getRGB((int) Math.ceil(x), (int) Math.ceil(y));
                alpha = x - (int) x;
                cA = (int) ((1 - alpha) * ((tl & 0xff000000) >> 24) + alpha * ((tr & 0xff000000) >> 24)) << 24;
                cA += (int) ((1 - alpha) * ((tl & 0xff0000) >> 16) + alpha * ((tr & 0xff0000) >> 16)) << 16;
                cA += (int) ((1 - alpha) * ((tl & 0x00ff00) >> 8) + alpha * ((tr & 0x00ff00) >> 8)) << 8;
                cA += (int) ((1 - alpha) * (tl & 0x0000ff) + alpha * (tr & 0x0000ff));

                cB = (int) ((1 - alpha) * ((bl & 0xff000000) >> 24) + alpha * ((br & 0xff000000) >> 24)) << 24;
                cB += (int) ((1 - alpha) * ((bl & 0xff0000) >> 16) + alpha * ((br & 0xff0000) >> 16)) << 16;
                cB += (int) ((1 - alpha) * ((bl & 0x00ff00) >> 8) + alpha * ((br & 0x00ff00) >> 8)) << 8;
                cB += (int) ((1 - alpha) * (bl & 0x0000ff) + alpha * (br & 0x0000ff));

                beta = y - (int) y;
                cD = (int) ((1 - beta) * ((cA & 0xff000000) >> 24) + beta * ((cB & 0xff000000) >> 24)) << 24;
                cD += (int) ((1 - beta) * ((cA & 0xff0000) >> 16) + beta * ((cB & 0xff0000) >> 16)) << 16;
                cD += (int) ((1 - beta) * ((cA & 0x00ff00) >> 8) + beta * ((cB & 0x00ff00) >> 8)) << 8;
                cD += (int) ((1 - beta) * (cA & 0x0000ff) + beta * (cB & 0x0000ff));

//                cD += 0xff000000;

                newImage.setRGB(i, j, cD);
            }
        }
//        try {
//            File outputfile = new File("preview.png");
//            ImageIO.write(syntheticImage, "png", outputfile);
//        }catch (Exception e) {
//
//        }

        modifiedImage = newImage;
//        this.translateX = translateX;
//        this.translateY = translateY;
        calculateOriginsPoint(transformationMatrix, originWidth, originHeight);
    }

    private void calculateOriginsPoint(double[][] transformationMatrix, double originWidth, double originHeight) {
        //calculate origins point
        Point2D p1 = multiply(originTranslateX, originTranslateY, transformationMatrix);
        Point2D p2 = multiply(originTranslateX, originHeight - 1 + originTranslateY, transformationMatrix);
        Point2D p3 = multiply(originWidth - 1 + originTranslateX, originTranslateY, transformationMatrix);
        Point2D p4 = multiply(originWidth - 1 + originTranslateX, originHeight - 1 + originTranslateY, transformationMatrix);
        double translateX = p1.getX();
        if (p2.getX() < translateX)
            translateX = p2.getX();
        if (p3.getX() < translateX)
            translateX = p3.getX();
        if (p4.getX() < translateX)
            translateX = p4.getX();
        double translateY = p1.getY();
        if (p2.getY() < translateY)
            translateY = p2.getY();
        if (p3.getY() < translateY)
            translateY = p3.getY();
        if (p4.getY() < translateY)
            translateY = p4.getY();
        this.translateX = translateX;
        this.translateY = translateY;
    }

    public BufferedImage getImage() {
        return modifiedImage;
    }

    public BufferedImage getOriginalImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        this.translateX = 0;
        this.translateY = 0;
    }

    public void updateImage(BufferedImage modifiedImage) {
        this.modifiedImage = modifiedImage;
    }

    public double getTranslateX() {
        return translateX;
    }

    public double getTranslateY() {
        return translateY;
    }

    public void setTranslateX(double translateX) {
        this.translateX = translateX;
    }

    public void setTranslateY(double translateY) {
        this.translateY = translateY;
    }

    public double getOriginTranslateX() {
        return originTranslateX;
    }

    public void setOriginTranslateX(double originTranslateX) {
        this.originTranslateX = originTranslateX;
    }

    public double getOriginTranslateY() {
        return originTranslateY;
    }

    public void setOriginTranslateY(double originTranslateY) {
        this.originTranslateY = originTranslateY;
    }

    public Point2D multiply(double x, double y, double[][] transformationMatrix) {
        double[][] M = {
                {x},
                {y},
                {1}};
        double[][] result = MatrixHelper.multiply(transformationMatrix, M);

        //scale
        double s = result[2][0];

        x = result[0][0] / s;
        y = result[1][0] / s;
        return new Point2D(x, y);
    }
}
