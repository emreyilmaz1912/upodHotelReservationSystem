package com.emreyilmaz.upodhotelreservationsystem.controllers.deletedItemsController;


import com.emreyilmaz.upodhotelreservationsystem.DBUtils;
import com.emreyilmaz.upodhotelreservationsystem.ScreenManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.emreyilmaz.upodhotelreservationsystem.DBUtils.*;


public class DeleteCustomerController implements Initializable {
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
    private TextField txt_searching;
    @FXML
    private Button buttonNewCustomer;
    @FXML
    private Button buttonEditCustomer;
    @FXML
    private Button buttonDeleteCustomer;

    @FXML
    private ComboBox<String> customersComboBox;
    @FXML
    private Button logOut;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeButtons();
        populateCustomerComboBox();
    }

    private void initializeButtons() {
        buttonRooms.setOnAction(e -> ScreenManager.showRoomsPage(buttonRooms.getScene()));
        buttonFeatures.setOnAction(e -> ScreenManager.showFeaturesPage(buttonFeatures.getScene()));
        buttonCustomers.setOnAction(e -> ScreenManager.showCustomersPage(buttonCustomers.getScene()));
        buttonServices.setOnAction(e -> ScreenManager.showServicesPage(buttonServices.getScene()));
        reservationsServicesButton.setOnAction(e -> ScreenManager.showReservationsServicesPage(reservationsServicesButton.getScene()));
        mainPage.setOnAction(e -> ScreenManager.showMainPage(mainPage.getScene()));
        buttonDeleteCustomer.setOnAction(e -> ScreenManager.showDeleteCustomerPage(buttonDeleteCustomer.getScene()));
        buttonEditCustomer.setOnAction(e -> ScreenManager.showEditCustomerPage(buttonEditCustomer.getScene()));
        buttonNewCustomer.setOnAction(e -> ScreenManager.showNewCustomerPage(buttonNewCustomer.getScene()));
        logOut.setOnAction(e -> ScreenManager.showLogOut(logOut.getScene()));
    }

    public void handleDeleteCustomerButtonClick(ActionEvent event) {
        String selectedCustomer = customersComboBox.getValue();
        if (selectedCustomer != null) {
            deleteCustomer(selectedCustomer);
        }
    }

    private void populateCustomerComboBox() {
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT FullName FROM customers")) {

            var resultSet = preparedStatement.executeQuery();
            ObservableList<String> customerNames = FXCollections.observableArrayList();

            while (resultSet.next()) {
                customerNames.add(resultSet.getString("fullName"));
            }

            customersComboBox.setItems(customerNames);

        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
    }

    private void deleteCustomer(String selectedCustomer) {
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD)) {
            String sql = "DELETE FROM customers WHERE FullName=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, selectedCustomer);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Customer deleted successfully.");
                    DBUtils.showSuccessAlert("Success", "Customer Deleted", "Customer has been successfully deleted.");
                } else {
                    System.out.println("Failed to delete customer.");
                    DBUtils.showErrorAlert("Error", "Failed to Delete Customer", "Failed to delete the customer.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
    }
}
