package com.example.imageproject;

import javafx.scene.control.*;
import javafx.scene.image.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;

public class MainController {
    private static ImageView imageViewOriginal = new ImageView();
    private static ImageView imageViewCopy = new ImageView();
    private static BufferedImage selectedImage;
    private static BufferedImage processedImageBuffered;

    public static void loadImage(Button button) {
        try {
            selectedImage = Files.loadImageFromFile();
            if (selectedImage != null) {
                Image fxImage = SwingFXUtils.toFXImage(selectedImage, null);
                imageViewOriginal.setImage(fxImage);
                imageViewOriginal.setPreserveRatio(true);
                imageViewOriginal.setFitWidth(400);

                imageViewCopy.setImage(null);
                processedImageBuffered = null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void setFilter(ComboBox comboBox) {
        if (selectedImage == null || comboBox.getValue() == null) {
            System.out.println("No image or filter selected.");
            return;
        }

        String effect = comboBox.getValue().toString().toLowerCase();
        Image fxImage = SwingFXUtils.toFXImage(selectedImage, null);
        Image processedImage = ProcessorOfImage.processImage(fxImage, effect);

        if (processedImage != null) {
            imageViewCopy.setImage(processedImage);
            imageViewCopy.setPreserveRatio(true);
            imageViewCopy.setFitWidth(400);

            processedImageBuffered = SwingFXUtils.fromFXImage(processedImage, null);
        } else {
            System.out.println("Failed to apply filter: " + effect);
        }
    }

    public static BufferedImage getSelectedImage() {
        return selectedImage;
    }

    public static void setSelectedImage(BufferedImage image) {
        selectedImage = image;
    }

    public static BufferedImage getProcessedImage() {
        return processedImageBuffered;
    }

    public static ImageView getImageViewOriginal() {
        return imageViewOriginal;
    }

    public static ImageView getImageViewCopy() {
        return imageViewCopy;
    }

    public static void setImageViewCopy(ImageView imageViewCopy) {
        MainController.imageViewCopy = imageViewCopy;
    }
}
