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


public class DeleteFeatureController implements Initializable {
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
    private TextField featureNameTextField;
    @FXML
    private ComboBox<String> featuresComboBox;

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
        initializeComboBox();
    }
    private void initializeButtons() {
        buttonRooms.setOnAction(e -> ScreenManager.showRoomsPage(buttonRooms.getScene()));
        buttonFeatures.setOnAction(e -> ScreenManager.showFeaturesPage(buttonFeatures.getScene()));
        buttonCustomers.setOnAction(e -> ScreenManager.showCustomersPage( buttonCustomers.getScene()));
        buttonServices.setOnAction(e -> ScreenManager.showServicesPage(buttonServices.getScene()));
        reservationsServicesButton.setOnAction(e -> ScreenManager.showReservationsServicesPage(reservationsServicesButton.getScene()));
        mainPage.setOnAction(e -> ScreenManager.showMainPage( mainPage.getScene()));
        buttonDeleteFeature.setOnAction(e -> ScreenManager.showDeleteFeaturePage( buttonDeleteFeature.getScene()));
        buttonEditFeature.setOnAction(e -> ScreenManager.showEditFeaturePage(buttonEditFeature.getScene()));
        buttonNewFeature.setOnAction(e -> ScreenManager.showNewFeaturePage( buttonNewFeature.getScene()));
        logOut.setOnAction(e -> ScreenManager.showLogOut(logOut.getScene()));
    }


    @FXML
    public void handleDeleteFeatureButtonClick(ActionEvent event) {
        String selectedFeature = featuresComboBox.getSelectionModel().getSelectedItem();
        String enteredFeatureName = featureNameTextField.getText();

        if ((selectedFeature != null && !selectedFeature.isEmpty()) || (enteredFeatureName != null && !enteredFeatureName.isEmpty())) {
            deleteFeature(selectedFeature, enteredFeatureName);
        } else {
            DBUtils.showErrorAlert("Error", "Invalid Input", "Please select a feature or enter a valid feature name.");
        }
    }

    private void deleteFeature(String selectedFeature, String enteredFeatureName) {
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD)) {
            String sql;
            if (selectedFeature != null && !selectedFeature.isEmpty()) {
                sql = "DELETE FROM features WHERE FeatureName = ?";
            } else {
                sql = "DELETE FROM features WHERE FeatureName = ?";
                selectedFeature = enteredFeatureName;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, selectedFeature);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    DBUtils.showSuccessAlert("Success", "Feature Deleted", "Feature has been successfully deleted.");
                    initializeComboBox();
                } else {
                    DBUtils.showErrorAlert("Error", "Failed to Delete Feature", "Failed to delete the feature.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initializeComboBox() {
        ObservableList<String> featureNames = getAllFeatureNames();
        featuresComboBox.setItems(featureNames);
    }

    private ObservableList<String> getAllFeatureNames() {
        ObservableList<String> featureNames = FXCollections.observableArrayList();
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD)) {
            String sql = "SELECT FeatureName FROM features";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String featureName = resultSet.getString("featureName");
                        featureNames.add(featureName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return featureNames;
    }
}
