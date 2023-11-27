package com.emreyilmaz.upodhotelreservationsystem.controllers.editItemsController;


import com.emreyilmaz.upodhotelreservationsystem.DBUtils;
import com.emreyilmaz.upodhotelreservationsystem.ScreenManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static com.emreyilmaz.upodhotelreservationsystem.DBUtils.*;


public class EditCustomerController implements Initializable {
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
    private Button buttonNewCustomer;
    @FXML
    private Button buttonEditCustomer;
    @FXML
    private Button buttonDeleteCustomer;
    @FXML
    private ComboBox<String> customersComboBox;

    @FXML
    private TextField tf_customerFullName;

    @FXML
    private TextField tf_customerIdentityNumber;

    @FXML
    private TextField tf_customerPhoneNumber;

    @FXML
    private DatePicker dt_customerBirthDate;

    @FXML
    private TextField tf_customerDescription;
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

    public void handleEditCustomerButtonClick(ActionEvent event) {
        String selectedCustomer = customersComboBox.getValue();
        String fullName = tf_customerFullName.getText();
        String identityNumber = tf_customerIdentityNumber.getText();
        String phoneNumber = tf_customerPhoneNumber.getText();
        String birthDate = (dt_customerBirthDate.getValue() != null) ? dt_customerBirthDate.getValue().toString() : null;
        String description = tf_customerDescription.getText();
        editCustomer(selectedCustomer, fullName, identityNumber, phoneNumber, birthDate, description);
    }

    private void editCustomer(String selectedCustomer, String fullName, String identityNumber, String phoneNumber, String birthDate, String description) {
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD)) {
            String sql = "UPDATE customers SET fullName=?, IdentityNumber=?, PhoneNumber=?, BirthDate=?, Description=? WHERE fullName=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, fullName);
                preparedStatement.setString(2, identityNumber);
                preparedStatement.setString(3, phoneNumber);
                preparedStatement.setString(4, birthDate);
                preparedStatement.setString(5, description);
                preparedStatement.setString(6, selectedCustomer);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Customer updated successfully.");
                    DBUtils.showSuccessAlert("Success", "Customer Updated", "Customer has been successfully updated.");
                } else {
                    System.out.println("Failed to update customer.");
                    DBUtils.showErrorAlert("Error", "Failed to Update Customer", "Failed to update the customer.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
    }


    private void populateCustomerComboBox() {
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT fullName FROM customers")) {

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
}
