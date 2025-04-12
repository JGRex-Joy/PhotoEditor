package com.example.imageproject;

import javafx.stage.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.application.*;
import javafx.collections.*;

public class HelloApplication extends Application {
    public Button openButton = new Button("Open File");
    Button processButton = new Button("Process");
    Button saveButton = new Button("Save");

    ObservableList<String> options = FXCollections.observableArrayList("Grayscale", "Inverse", "Blurring");
    ComboBox cmb = new ComboBox(options);

    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #ffffff;");

        HBox buttons = new HBox(15);
        buttons.setStyle("-fx-padding: 10px 15px");
        buttons.getChildren().addAll(openButton, cmb, processButton, saveButton);

        openButton.setStyle("-fx-padding: 12px 15px; -fx-font-size: 15px;");
        processButton.setStyle("-fx-padding: 12px 15px; -fx-font-size: 15px;");
        cmb.setStyle("-fx-padding: 6px 11px; -fx-font-size: 15px;");
        saveButton.setStyle("-fx-padding: 12px 15px; -fx-font-size: 15px;");
        buttons.setStyle("-fx-background-color: #ffffff; -fx-border-width: 1px; -fx-padding: 8px 14px");


        openButton.setOnAction(e -> {
            MainController.loadImage(openButton);
        });

        processButton.setOnAction(e -> {
            String selectedEffect = (String) cmb.getValue();
            if (selectedEffect == null) {
                System.out.println("Select a filter option");
                return;
            }

            Image original = MainController.getImageViewOriginal().getImage();
            if (original == null) {
                System.out.println("No image loaded");
                return;
            }

            switch (selectedEffect.toLowerCase()) {
                case "grayscale", "inverse", "blurring" ->
                    MainController.setFilter(cmb);
            }
            ProcessorOfImage.blur(original);
        });


        saveButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                    new FileChooser.ExtensionFilter("JPG Files", "*.jpg")
            );
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    BufferedImage imageToSave = MainController.getProcessedImage();
                    if (imageToSave == null) {
                        imageToSave = MainController.getSelectedImage();
                    }
                    Files.saveImageToFile(imageToSave, file);;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        Label originalLabel = new Label("Original");
        originalLabel.setStyle("-fx-font-size: 14px;");

        Label resultLabel = new Label("Result");
        resultLabel.setStyle("-fx-font-size: 14px;");

        VBox originalPhotoContainer = new VBox(10, originalLabel, MainController.getImageViewOriginal());
        originalPhotoContainer.setMinWidth(400);

        VBox resultPhotoContainer = new VBox(10, resultLabel, MainController.getImageViewCopy());
        resultPhotoContainer.setMinWidth(400);

        HBox photos = new HBox(15, originalPhotoContainer, resultPhotoContainer);
        photos.setStyle("-fx-padding: 15px");

        root.getChildren().add(buttons);
        root.getChildren().add(photos);

        Scene scene = new Scene(root, 900, 500);
        Platform.runLater(() -> root.requestFocus());

        stage.setTitle("Photo Editor Amir SCA-24A");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}