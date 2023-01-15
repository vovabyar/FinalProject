package UI.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * @author vovabyar
 */
public class NameColumnEntry {
    private StringProperty fileName;
    private Image icon;

    public NameColumnEntry(String fileName, Icon swingIcon) {
        this.fileName = new SimpleStringProperty(fileName);
        this.icon = convertIcon(swingIcon);
    }

    public String getFileName() {
        return fileName.get();
    }

    public StringProperty fileNameProperty() {
        return fileName;
    }

    public Image getIcon() {
        return icon;
    }
    private static Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }

        return new ImageView(wr).getImage();
    }
    private static Image convertIcon(Icon swingIcon) {
        BufferedImage bufferedImage = new BufferedImage(swingIcon.getIconWidth(), swingIcon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB);
        swingIcon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
        return convertToFxImage(bufferedImage);
    }
}