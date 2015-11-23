package logic;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by bider_000 on 14.11.2015.
 */
public class BackgroundDrawer implements IBackgroundsDrawer {

    @Override
    public BackgroundImage draw(double width, double height) {
        BufferedImage image = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);

        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();
        final int black = 0xff000000;
        final int white = 0xffffffff;
        final int gap = 10;

        int centerX;
        int centerY;
        if (imgWidth % 2 == 0)
            centerX = imgWidth / 2;
        else
            centerX = imgWidth / 2 + 1;
        if (imgHeight % 2 == 0)
            centerY = imgHeight / 2;
        else
            centerY = imgHeight / 2 + 1;

//        for (int i = 0; i < imgWidth; i++) {
//            for (int j = 0; j < imgHeight; j++) {
//                image.setRGB(i, j, white);
//            }
//        }

        int xOffset = centerX % gap;
        int yOffset = centerY % gap;

        for (int i = xOffset; i < imgWidth; i += gap) {
            for (int j = yOffset; j < imgHeight; j += gap) {
                image.setRGB(i, j, black);
            }
        }
        for (int i = 0; i < imgWidth; i++) {
            image.setRGB(i, centerY, black);
        }
        for (int i = 0; i < imgHeight; i++) {
        image.setRGB(centerX, i, black);
    }


        Image img = SwingFXUtils.toFXImage(image, null);
        BackgroundImage bgImage = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

        try

        {
            File f = new File("MyFile.jpg");
            ImageIO.write(image, "PNG", f);
        } catch (
                Exception e
                )

        {

        }

        return bgImage;
    }
}
