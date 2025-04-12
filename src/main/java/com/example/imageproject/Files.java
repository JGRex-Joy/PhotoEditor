package com.example.imageproject;

import java.io.*;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Files {
    public static BufferedImage loadImageFromFile() throws IOException {
        try{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Image File");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));
            File file = fileChooser.showOpenDialog(null);
            if(file != null){
                BufferedImage selectedImage = ImageIO.read(file);
                System.out.println(selectedImage);
                return selectedImage;
            } else {
                return null;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static void saveImageToFile(BufferedImage image, File file) throws IOException {
        String fileName = file.getName().toLowerCase();
        String format;
        if (fileName.endsWith(".png")) {
            format = "png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            format = "jpg";
            BufferedImage rgbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            rgbImage.getGraphics().drawImage(image, 0, 0, null);
            image = rgbImage;
        } else {
            System.out.println("Unsupported file format. Use .jpg or .png");
            return;
        }

        boolean success = ImageIO.write(image, format, file);
        if (success) {
            System.out.println("Saved image to" + file.getAbsolutePath());
        } else {
            System.out.println("Fail to save image");
        }
    }

}
