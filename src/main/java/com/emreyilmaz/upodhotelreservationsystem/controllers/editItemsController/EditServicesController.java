package com.emreyilmaz.upodhotelreservationsystem.controllers.editItemsController;


import com.emreyilmaz.upodhotelreservationsystem.DBUtils;
import com.emreyilmaz.upodhotelreservationsystem.ScreenManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static com.emreyilmaz.upodhotelreservationsystem.DBUtils.*;


public class EditServicesController implements Initializable {
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
    private Button reservationsServicesButton;
    @FXML
    private Button buttonNewService;
    @FXML
    private Button buttonEditService;
    @FXML
    private Button buttonDeleteService;
    @FXML
    private ComboBox<String> serviceComboBox;

    @FXML
    private TextField editServiceNameTextField;

    @FXML
    private TextField editServicePriceTextField;
    @FXML
    private Button logOut;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> serviceNames = getAllServiceNames();
        serviceComboBox.setItems(serviceNames);
        initializeButtons();
    }

    private void initializeButtons() {
        buttonRooms.setOnAction(e -> ScreenManager.showRoomsPage(buttonRooms.getScene()));
        buttonFeatures.setOnAction(e -> ScreenManager.showFeaturesPage(buttonFeatures.getScene()));
        buttonCustomers.setOnAction(e -> ScreenManager.showCustomersPage(buttonCustomers.getScene()));
        buttonServices.setOnAction(e -> ScreenManager.showServicesPage(buttonServices.getScene()));
        reservationsServicesButton.setOnAction(e -> ScreenManager.showReservationsServicesPage(reservationsServicesButton.getScene()));
        mainPage.setOnAction(e -> ScreenManager.showMainPage(mainPage.getScene()));
        buttonDeleteService.setOnAction(e -> ScreenManager.showDeleteServicePage(buttonDeleteService.getScene()));
        buttonEditService.setOnAction(e -> ScreenManager.showEditServicePage(buttonEditService.getScene()));
        buttonNewService.setOnAction(e -> ScreenManager.showNewServicePage(buttonNewService.getScene()));
        logOut.setOnAction(e -> ScreenManager.showLogOut(logOut.getScene()));

    }

    @FXML
    private void handleEditServicesButtonClick() {
        String selectedService = serviceComboBox.getValue();
        String newServiceName = editServiceNameTextField.getText();
        String newServicePriceString = editServicePriceTextField.getText();

        try {
            double newServicePrice = Double.parseDouble(newServicePriceString);
            updateService(selectedService, newServiceName, newServicePrice);
            DBUtils.showSuccessAlert("Success", "Service Updated", "Service has been successfully updated.");
            ObservableList<String> updatedServiceNames = getAllServiceNames();
            serviceComboBox.setItems(updatedServiceNames);
        } catch (NumberFormatException e) {
            DBUtils.showErrorAlert("Error", "Invalid Input", "Please enter a valid numeric value for New Service Price.");
        }
    }

    private void updateService(String selectedService, String newServiceName, double newServicePrice) {
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD)) {
            String sql = "UPDATE services SET ServiceName = ?, ServicePrice = ? WHERE ServiceName = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newServiceName);
                preparedStatement.setDouble(2, newServicePrice);
                preparedStatement.setString(3, selectedService);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Failed to Update Service", "Failed to update the service in the database.");
        }
    }

    private ObservableList<String> getAllServiceNames() {
        ObservableList<String> serviceNames = FXCollections.observableArrayList();
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD)) {
            String sql = "SELECT ServiceName FROM services";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String serviceName = resultSet.getString("ServiceName");
                        serviceNames.add(serviceName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return serviceNames;
    }
}
