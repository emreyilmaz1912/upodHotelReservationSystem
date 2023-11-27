package com.emreyilmaz.upodhotelreservationsystem;



import com.emreyilmaz.upodhotelreservationsystem.controllers.newItemsController.*;
import com.emreyilmaz.upodhotelreservationsystem.controllers.*;
import com.emreyilmaz.upodhotelreservationsystem.controllers.deletedItemsController.*;
import com.emreyilmaz.upodhotelreservationsystem.controllers.editItemsController.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;



public class ScreenManager {


    public static void changeScene(String fxmlPath, Window window) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            Scene newScene = new Scene(root);
            Stage stage = (Stage) window;
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
public static void showPage(String fxmlName, Scene currentScene) {
    try {
        FXMLLoader loader = new FXMLLoader(ScreenManager.class.getResource(fxmlName));
        Parent root = loader.load();

        currentScene.setRoot(root);

    } catch (IOException e) {
        e.printStackTrace();
    }
}

public static void switchScene(String fxmlPath, Scene currentScene) {
    try {
        FXMLLoader loader = new FXMLLoader(ScreenManager.class.getResource(fxmlPath));
        Parent root = loader.load();

        currentScene.setRoot(root);

    } catch (IOException e) {
        e.printStackTrace();
    }
}
    public static void showLogOut(Scene currentScene) {
        switchScene("main-login.fxml", currentScene);
    }
    public static void showRoomsPage(Scene currentScene) {
        switchScene("room-screen.fxml", currentScene);
    }

    public static void showFeaturesPage(Scene currentScene) {
        switchScene("feature-screen.fxml", currentScene);
    }

    public static void showServicesPage(Scene currentScene) {
        switchScene("service-screen.fxml", currentScene);
    }

    public static void showCustomersPage(Scene currentScene) {
        switchScene("customer-screen.fxml", currentScene);
    }
    public static void showEditCustomerPage(Scene currentScene) {
        switchScene("edit-customer.fxml", currentScene);
    }

    public static void showReservationsServicesPage(Scene currentScene) {
        switchScene("reservations-services.fxml", currentScene);
    }

    public static void showNewReservationPage(Scene currentScene) {
        switchScene("new-reservation.fxml", currentScene);
    }


    public static void showDeleteReservationPage(Scene currentScene) {
        switchScene("delete-reservation.fxml", currentScene);
    }

    public static void showNewRoomPage(Scene currentScene) {
        switchScene("new-room.fxml", currentScene);
    }

    public static void showMainPage(Scene currentScene) {
        switchScene("main-screen.fxml", currentScene);
    }

    public static void showEditRoomPage(Scene currentScene) {
        switchScene("edit-room.fxml", currentScene);
    }
    public static void showEditReservationPage(Scene currentScene) {
        switchScene("edit-reservation.fxml", currentScene);
    }

    public static void showDeleteRoomPage(Scene currentScene) {
        switchScene("delete-room.fxml", currentScene);
    }

    public static void showNewFeaturePage(Scene currentScene) {
        switchScene("new-feature.fxml", currentScene);
    }

    public static void showEditFeaturePage(Scene currentScene) {
        switchScene("edit-feature.fxml", currentScene);
    }

    public static void showDeleteFeaturePage(Scene currentScene) {
        switchScene("delete-feature.fxml", currentScene);
    }

    public static void showNewServicePage(Scene currentScene) {
        switchScene("new-services.fxml", currentScene);
    }

    public static void showEditServicePage(Scene currentScene) {
        switchScene("edit-services.fxml", currentScene);
    }

    public static void showDeleteServicePage(Scene currentScene) {
        switchScene("delete-services.fxml", currentScene);
    }

    public static void showNewCustomerPage(Scene currentScene) {
        switchScene("new-customer.fxml", currentScene);
    }

    public static void showDeleteCustomerPage(Scene currentScene) {
        switchScene("delete-customer.fxml", currentScene);
    }

    public static void showNewReservationServicePage(Scene currentScene) {
        switchScene("new-reservations-services.fxml", currentScene);
    }

    public static void showEditReservationServicePage(Scene currentScene) {
        switchScene("edit-reservation-services.fxml", currentScene);
    }

    public static void showDeleteReservationServicePage(Scene currentScene) {
        switchScene("delete-reservation-services.fxml", currentScene);
    }

    public static void showLogInPage(Scene currentScene) {
        switchScene("log-in.fxml", currentScene);
    }

}

