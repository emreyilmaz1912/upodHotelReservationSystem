package com.emreyilmaz.upodhotelreservationsystem.controllers;

import com.emreyilmaz.upodhotelreservationsystem.DBUtils;
import com.emreyilmaz.upodhotelreservationsystem.Features;
import com.emreyilmaz.upodhotelreservationsystem.ScreenManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class FeatureController implements Initializable {
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
    private TableView<Features> featuresTableView;
    @FXML
    private TableColumn<Features, Integer> featureId;
    @FXML
    private TableColumn<Features, String> featureName;
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
        initializeTable();
        initializeButtons();
    }

    private void initializeTable() {
        featureId.setCellValueFactory(new PropertyValueFactory<>("featureId"));
        featureName.setCellValueFactory(new PropertyValueFactory<>("featureName"));

        List<Features> features = DBUtils.getAllFeatures();
        if (features != null) {
            ObservableList<Features> observableFeatures = FXCollections.observableArrayList(features);
            featuresTableView.setItems(observableFeatures);
        } else {
            System.out.println("An error occurred while retrieving features from the database.");
        }
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
}
