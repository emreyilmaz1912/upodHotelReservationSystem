package com.emreyilmaz.upodhotelreservationsystem.controllers.newItemsController;

import com.emreyilmaz.upodhotelreservationsystem.DBUtils;
import com.emreyilmaz.upodhotelreservationsystem.ScreenManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.emreyilmaz.upodhotelreservationsystem.DBUtils.*;


public class NewCustomerController implements Initializable {
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
    private TextField tf_customerFullName;
    @FXML
    private TextField tf_customerPhoneNumber;
    @FXML
    private TextField tf_customerIdentityNumber;
    @FXML
    private TextField tf_customerDescription;
    @FXML
    private DatePicker dt_customerBirthDate;
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
        buttonDeleteCustomer.setOnAction(e -> ScreenManager.showDeleteCustomerPage(buttonDeleteCustomer.getScene()));
        buttonEditCustomer.setOnAction(e -> ScreenManager.showEditCustomerPage(buttonEditCustomer.getScene()));
        buttonNewCustomer.setOnAction(e -> ScreenManager.showNewCustomerPage(buttonNewCustomer.getScene()));
        logOut.setOnAction(e -> ScreenManager.showLogOut(logOut.getScene()));
    }


    public void handleNewCustomerButtonClick(ActionEvent event) {
        String fullName = tf_customerFullName.getText();
        String identityNumber = tf_customerIdentityNumber.getText();
        String phoneNumber = tf_customerPhoneNumber.getText();
        String birthDate = (dt_customerBirthDate.getValue() != null) ? dt_customerBirthDate.getValue().toString() : null;
        String description = tf_customerDescription.getText();

        saveNewCustomer(fullName, identityNumber, phoneNumber, birthDate, description);
    }

    private void saveNewCustomer(String fullName, String identityNumber, String phoneNumber, String birthDate, String description) {
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD)) {
            String sql = "INSERT INTO customers (FullName, IdentityNumber, PhoneNumber, BirthDate, Description) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, fullName);
                preparedStatement.setString(2, identityNumber);
                preparedStatement.setString(3, phoneNumber);
                preparedStatement.setString(4, birthDate);
                preparedStatement.setString(5, description);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("New customer saved successfully.");
                    DBUtils.showSuccessAlert("Success", "New Customer Saved", "New customer has been successfully saved.");
                } else {
                    System.out.println("Failed to save new customer.");
                    DBUtils.showErrorAlert("Error", "Failed to Save New Customer", "Failed to save the new customer.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
    }
}