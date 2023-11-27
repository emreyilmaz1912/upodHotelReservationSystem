package com.emreyilmaz.upodhotelreservationsystem.controllers;


import com.emreyilmaz.upodhotelreservationsystem.Customers;
import com.emreyilmaz.upodhotelreservationsystem.DBUtils;
import com.emreyilmaz.upodhotelreservationsystem.ScreenManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;


public class CustomerController implements Initializable {
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
    private TextField tf_searchCustomersByName;
    @FXML
    private TableView<Customers> customersTableView;
    @FXML
    private TableColumn<Customers, Integer> columnId;
    @FXML
    private TableColumn<Customers, String> fullName;
    @FXML
    private TableColumn<Customers, String> identityNumber;
    @FXML
    private TableColumn<Customers, String> phoneNumber;
    @FXML
    private TableColumn<Customers, Date> birthDate;
    @FXML
    private TableColumn<Customers, String> description;
    @FXML
    private Button buttonNewCustomer;
    @FXML
    private Button buttonEditCustomer;
    @FXML
    private Button buttonDeleteCustomer;
    @FXML
    private Button logOut;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        initializeButtons();
    }

    private void initializeTable() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        fullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        identityNumber.setCellValueFactory(new PropertyValueFactory<>("identityNumber"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        birthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));

        List<Customers> customers = DBUtils.getAllCustomers();
        if (customers != null) {
            ObservableList<Customers> observableCustomers = FXCollections.observableArrayList(customers);
            customersTableView.setItems(observableCustomers);
        } else {
            System.out.println("An error occurred while retrieving customers from the databases.");
        }
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

    public void handleSearchCustomersByNameButtonClick(ActionEvent event) {
        String customerName = tf_searchCustomersByName.getText();
        if (customerName != null) {
            List<Customers> filteredCustomers = customersTableView.getItems().filtered(x -> x.getFullName().contains(customerName));
            ObservableList<Customers> observableFilteredCustomers = FXCollections.observableArrayList(filteredCustomers);
            customersTableView.setItems(observableFilteredCustomers);

        } else {
            DBUtils.showErrorAlert("Error", "Customer not found", "Please enter a valid customer name.");
        }
    }


}
