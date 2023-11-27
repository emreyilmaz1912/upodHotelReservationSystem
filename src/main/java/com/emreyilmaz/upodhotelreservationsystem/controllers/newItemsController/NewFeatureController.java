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


public class NewFeatureController implements Initializable {
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
    private TextField newFeatureTextField;
    @FXML
    private Button saveNewFeatureButton;
    @FXML
    private Button buttonNewFeature;
    @FXML
    private Button buttonEditFeature;
    @FXML
    private Button buttonDeleteFeature;
    @FXML
    private Button logOut;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeButtons();
        saveNewFeatureButton.setOnAction(this::handleSaveNewFeatureButtonClick);
    }

    private void initializeButtons() {
        buttonRooms.setOnAction(e -> ScreenManager.showRoomsPage(buttonRooms.getScene()));
        buttonFeatures.setOnAction(e -> ScreenManager.showFeaturesPage(buttonFeatures.getScene()));
        buttonCustomers.setOnAction(e -> ScreenManager.showCustomersPage(buttonCustomers.getScene()));
        buttonServices.setOnAction(e -> ScreenManager.showServicesPage(buttonServices.getScene()));
        reservationsServicesButton.setOnAction(e -> ScreenManager.showReservationsServicesPage(reservationsServicesButton.getScene()));
        mainPage.setOnAction(e -> ScreenManager.showMainPage(mainPage.getScene()));
        buttonDeleteFeature.setOnAction(e -> ScreenManager.showDeleteFeaturePage(buttonDeleteFeature.getScene()));
        buttonEditFeature.setOnAction(e -> ScreenManager.showEditFeaturePage(buttonEditFeature.getScene()));
        buttonNewFeature.setOnAction(e -> ScreenManager.showNewFeaturePage(buttonNewFeature.getScene()));
        logOut.setOnAction(e -> ScreenManager.showLogOut(logOut.getScene()));
    }

    private void handleSaveNewFeatureButtonClick(ActionEvent event) {
        String newFeatureName = newFeatureTextField.getText();

        if (!newFeatureName.isEmpty()) {
            saveNewFeatureToDatabase(newFeatureName);
            clearFields();
        } else {

            DBUtils.showErrorAlert("Error", "Invalid Input", "Please enter a valid feature name.");
        }
    }


    private void saveNewFeatureToDatabase(String newFeatureName) {
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD)) {
            String sql = "INSERT INTO features (FeatureName) VALUES (?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newFeatureName);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    DBUtils.showSuccessAlert("Success", "New Feature Added", "New feature has been successfully added.");
                } else {
                    DBUtils.showErrorAlert("Error", "Failed to Add Feature", "Failed to add the new feature.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        newFeatureTextField.clear();
    }

    public void handleNewFeatureButtonClick(ActionEvent event) {
    }
}