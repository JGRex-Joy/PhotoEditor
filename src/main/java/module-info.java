module com.example.imageproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;


    opens com.example.imageproject to javafx.fxml;
    exports com.example.imageproject;
}