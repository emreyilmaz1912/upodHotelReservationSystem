package com.emreyilmaz.upodhotelreservationsystem.controllers.newItemsController;


import com.emreyilmaz.upodhotelreservationsystem.DBUtils;
import com.emreyilmaz.upodhotelreservationsystem.ScreenManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.emreyilmaz.upodhotelreservationsystem.DBUtils.*;


public class NewServicesController implements Initializable {
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
    private TextField serviceNameTextField;
    @FXML
    private TextField servicePriceTextField;
    @FXML
    private Button logOut;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

    public void handleNewServicesButtonClick(ActionEvent event) {
        String serviceName = serviceNameTextField.getText();
        String servicePriceString = servicePriceTextField.getText();

        try {
            double servicePrice = Double.parseDouble(servicePriceString);
            addNewService(serviceName, servicePrice);
            DBUtils.showSuccessAlert("Success", "Service Added", "Service has been successfully added.");
            clearTextFields();
        } catch (NumberFormatException e) {
            DBUtils.showErrorAlert("Error", "Invalid Input", "Please enter a valid numeric value for Service Price.");
        }
    }

    private void addNewService(String serviceName, double servicePrice) {
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD)) {
            String sql = "INSERT INTO services (ServiceName, ServicePrice) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, serviceName);
                preparedStatement.setDouble(2, servicePrice);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Failed to Add Service", "Failed to add the service to the database.");
        }
    }

    private void clearTextFields() {
        serviceNameTextField.clear();
        servicePriceTextField.clear();
    }
}
