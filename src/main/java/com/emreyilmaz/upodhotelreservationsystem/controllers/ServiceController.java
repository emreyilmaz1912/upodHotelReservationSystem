package com.emreyilmaz.upodhotelreservationsystem.controllers;


import com.emreyilmaz.upodhotelreservationsystem.DBUtils;
import com.emreyilmaz.upodhotelreservationsystem.ScreenManager;
import com.emreyilmaz.upodhotelreservationsystem.Services;
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


public class ServiceController implements Initializable {
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
    private TableView<Services> serviceTableView;
    @FXML
    private TableColumn<Services, Integer> serviceId;
    @FXML
    private TableColumn<Services, String> serviceName;
    @FXML
    private TableColumn<Services, Double> servicePrice;
    @FXML
    private Button buttonNewService;
    @FXML
    private Button buttonEditService;
    @FXML
    private Button buttonDeleteService;
    @FXML
    private Button logOut;

    public void initialize() {
        initializeTable();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeButtons();
        initializeTable();
    }

    private void initializeTable() {
        serviceId.setCellValueFactory(new PropertyValueFactory<>("ServiceId"));
        serviceName.setCellValueFactory(new PropertyValueFactory<>("ServiceName"));
        servicePrice.setCellValueFactory(new PropertyValueFactory<>("ServicePrice"));

        List<Services> services = DBUtils.getAllServices();
        ObservableList<Services> observableServices = FXCollections.observableArrayList(services);
        serviceTableView.setItems(observableServices);
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
}
