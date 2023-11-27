package com.emreyilmaz.upodhotelreservationsystem.controllers.deletedItemsController;

import com.emreyilmaz.upodhotelreservationsystem.DBUtils;
import com.emreyilmaz.upodhotelreservationsystem.ScreenManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static com.emreyilmaz.upodhotelreservationsystem.DBUtils.*;

public class DeleteReservationServicesController implements Initializable {
    @FXML
    private Button buttonRooms;
    @FXML
    private Button buttonFeatures;
    @FXML
    private Button buttonServices;
    @FXML
    private Button buttonCustomers;
    @FXML
    private Button mainPage;
    @FXML
    Button reservationsServicesButton;
    @FXML
    private Button buttonDeleteReservationService;
    @FXML
    private Button buttonNewReservationService;
    @FXML
    private Button buttonEditReservationService;

    @FXML
    private TextField tf_searchReservationServiceById;
    @FXML
    private ComboBox<Integer> reservationServiceIdComboBox;
    @FXML
    private Button logOut;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeButtons();
        populateReservationServiceIds();
    }

    private void initializeButtons() {
        buttonRooms.setOnAction(e -> ScreenManager.showRoomsPage(buttonRooms.getScene()));
        buttonFeatures.setOnAction(e -> ScreenManager.showFeaturesPage(buttonFeatures.getScene()));
        buttonCustomers.setOnAction(e -> ScreenManager.showCustomersPage(buttonCustomers.getScene()));
        buttonServices.setOnAction(e -> ScreenManager.showServicesPage(buttonServices.getScene()));
        reservationsServicesButton.setOnAction(e -> ScreenManager.showReservationsServicesPage(reservationsServicesButton.getScene()));
        mainPage.setOnAction(e -> ScreenManager.showMainPage(mainPage.getScene()));
        buttonDeleteReservationService.setOnAction(e -> ScreenManager.showDeleteReservationServicePage(buttonDeleteReservationService.getScene()));
        buttonEditReservationService.setOnAction(e -> ScreenManager.showEditReservationServicePage(buttonEditReservationService.getScene()));
        buttonNewReservationService.setOnAction(e -> ScreenManager.showNewReservationServicePage(buttonNewReservationService.getScene()));
        logOut.setOnAction(e -> ScreenManager.showLogOut(logOut.getScene()));
    }

    public void handleDeleteReservationsServicesButtonClick(ActionEvent event) {
        try {
            int reservationServiceId = Integer.parseInt(tf_searchReservationServiceById.getText());
            deleteReservationsServices(reservationServiceId);
        } catch (NumberFormatException e) {
            DBUtils.showErrorAlert("Error", "Invalid Input", "Please enter a valid ReservationServiceId");
        }
    }

    private void deleteReservationsServices(int reservationServiceId) {
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD)) {
            String sql = "DELETE FROM reservation_services WHERE reservationsServicesId=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, reservationServiceId);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Reservation service deleted successfully.");
                    DBUtils.showSuccessAlert("Success", "Reservation service Deleted", "Reservation service has been successfully deleted.");
                } else {
                    System.out.println("Failed to delete Reservation service.");
                    DBUtils.showErrorAlert("Error", "Failed to Delete Reservation service", "Failed to delete the Reservation service.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Failed to delete Reservation service", "Failed to delete the Reservation service from the database");
        }
    }

    private void populateReservationServiceIds() {
        ObservableList<Integer> reservationServiceIds = FXCollections.observableArrayList();
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD)) {
            String sql = "SELECT reservationsServicesId FROM reservation_services";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int reservationServiceId = resultSet.getInt("reservationsServicesId");
                        reservationServiceIds.add(reservationServiceId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        reservationServiceIdComboBox.setItems(reservationServiceIds);
    }

}
