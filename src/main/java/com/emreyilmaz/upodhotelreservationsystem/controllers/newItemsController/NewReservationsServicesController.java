package com.emreyilmaz.upodhotelreservationsystem.controllers.newItemsController;

import com.emreyilmaz.upodhotelreservationsystem.DBUtils;
import com.emreyilmaz.upodhotelreservationsystem.ScreenManager;
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

public class NewReservationsServicesController implements Initializable {
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
    private Button buttonDeleteReservationsService;
    @FXML
    private Button buttonNewReservationsService;
    @FXML
    private Button buttonEditReservationsService;
    @FXML
    private Button reservationsServicesButton;
    @FXML
    private TextField tf_reservationId;
    @FXML
    private TextField tf_serviceName;
    @FXML
    private TextField tf_quantity;
    @FXML
    private ComboBox<String> serviceNameComboBox;
    @FXML
    private ComboBox<String> reservationIdComboBox;
    @FXML
    private Button logOut;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateServiceNames();
        populateReservationIds();
        initializeButtons();

    }


    private void initializeButtons() {
        buttonRooms.setOnAction(e -> ScreenManager.showRoomsPage(buttonRooms.getScene()));
        buttonFeatures.setOnAction(e -> ScreenManager.showFeaturesPage(buttonFeatures.getScene()));
        buttonCustomers.setOnAction(e -> ScreenManager.showCustomersPage(buttonCustomers.getScene()));
        buttonServices.setOnAction(e -> ScreenManager.showServicesPage(buttonServices.getScene()));
        reservationsServicesButton.setOnAction(e -> ScreenManager.showReservationsServicesPage(reservationsServicesButton.getScene()));
        mainPage.setOnAction(e -> ScreenManager.showMainPage(mainPage.getScene()));
        buttonDeleteReservationsService.setOnAction(e -> ScreenManager.showDeleteReservationServicePage(buttonDeleteReservationsService.getScene()));
        buttonEditReservationsService.setOnAction(e -> ScreenManager.showEditReservationServicePage(buttonEditReservationsService.getScene()));
        buttonNewReservationsService.setOnAction(e -> ScreenManager.showNewReservationServicePage(buttonNewReservationsService.getScene()));
        logOut.setOnAction(e -> ScreenManager.showLogOut(logOut.getScene()));
    }

    private void populateReservationIds() {
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD)) {
            String sql = "SELECT reservationId FROM reservations";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int Id = resultSet.getInt("reservationId");
                        reservationIdComboBox.getItems().add(String.valueOf(Id));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
    }


    @FXML
    public void handleNewReservationsServicesButtonClick(ActionEvent event) {
        try {
            int reservationId = Integer.parseInt(tf_reservationId.getText());
            String serviceName = serviceNameComboBox.getValue();
            int quantity = Integer.parseInt(tf_quantity.getText());
            if (serviceName == null || serviceName.isEmpty()) {
                DBUtils.showErrorAlert("Error", "Service Name Not Selected", "Please select a service name.");
                return;
            }
            createReservationsService(reservationId, serviceName, quantity);
            clearFields();
            DBUtils.showSuccessAlert("Success", "Reservation Service Added", "Reservation service has been successfully added.");

        } catch (NumberFormatException e) {
            DBUtils.showErrorAlert("Error", "Invalid Input", "Please enter valid numeric values.");
        }
    }

    private void createReservationsService(int reservationId, String serviceName, int quantity) {
        int[] serviceInfo = getServiceIdAndServicePriceByName(serviceName);
        int serviceId = serviceInfo[0];
        double servicePrice = serviceInfo[1];

        if (serviceId != -1) {
            double unitPrice = servicePrice;
            double totalPrice = unitPrice * quantity;

            try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD)) {
                String sql = "INSERT INTO reservation_services (reservationId, serviceId, serviceName, unitPrice, quantity, totalPrice) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setInt(1, reservationId);
                    preparedStatement.setInt(2, serviceId);
                    preparedStatement.setString(3, serviceName);
                    preparedStatement.setDouble(4, unitPrice);
                    preparedStatement.setInt(5, quantity);
                    preparedStatement.setDouble(6, totalPrice);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Reservation service created successfully.");
                        DBUtils.showSuccessAlert("Success", "Reservation service Created", "Reservation service has been successfully created.");
                    } else {
                        System.out.println("Failed to create Reservation service.");
                        DBUtils.showErrorAlert("Error", "Failed to Create Reservation service", "Failed to create the Reservation service.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                DBUtils.showErrorAlert("Error", "Failed to create Reservation services", "Failed to create the reservation service in the database");
            }
        } else {
            System.out.println("ServiceId not found for ServiceName: " + serviceName);
        }
    }

    private void populateServiceNames() {
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD)) {
            String sql = "SELECT serviceName FROM services";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String serviceName = resultSet.getString("serviceName");
                        serviceNameComboBox.getItems().add(serviceName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
    }

    private void clearFields() {
        tf_reservationId.clear();
        tf_serviceName.clear();
        tf_quantity.clear();
    }

    private int[] getServiceIdAndServicePriceByName(String serviceName) {
        int[] result = {-1, -1};
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD)) {
            String sql = "SELECT serviceId, servicePrice FROM services WHERE serviceName = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, serviceName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        result[0] = resultSet.getInt("serviceId");
                        result[1] = (int) resultSet.getDouble("servicePrice");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
