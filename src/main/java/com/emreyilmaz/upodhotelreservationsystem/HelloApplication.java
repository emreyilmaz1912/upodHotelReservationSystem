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


public class HelloApplication extends Application {

    private ObservableList<Room> roomsList = FXCollections.observableArrayList();
    private ObservableList<Customers> customersList = FXCollections.observableArrayList();
    private ObservableList<Reservations> reservationsList = FXCollections.observableArrayList();
    private ObservableList<Features> featuresList = FXCollections.observableArrayList();
    private ObservableList<Services> servicesList = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-login.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Upod Hotel & Resort Reservation Manager System");
        Image icon = new Image("C:\\Users\\emrey\\Masaüstü\\Upod\\upodHotelReservationSystem\\src\\main\\resources\\com\\emreyilmaz\\upodhotelreservationsystem\\img\\icon_upod.png");
        stage.getIcons().add(icon);
        stage.setScene(scene);

        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
