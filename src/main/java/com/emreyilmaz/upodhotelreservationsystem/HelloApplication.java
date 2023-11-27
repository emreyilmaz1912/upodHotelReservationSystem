package com.emreyilmaz.upodhotelreservationsystem;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


import static com.emreyilmaz.upodhotelreservationsystem.Utils.LOGO;



public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-login.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Upod Hotel & Resort Reservation Manager System");

        Image icon = new Image(LOGO.toUri().toString());
        stage.getIcons().add(icon);
        stage.setScene(scene);

        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
