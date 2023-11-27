package com.emreyilmaz.upodhotelreservationsystem.controllers;


import com.emreyilmaz.upodhotelreservationsystem.DBUtils;
import com.emreyilmaz.upodhotelreservationsystem.ReservationServices;
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
import java.util.List;
import java.util.ResourceBundle;

import static com.emreyilmaz.upodhotelreservationsystem.DBUtils.*;


public class ReservationsServicesController implements Initializable {
    @FXML
    private Button buttonRooms;
    @FXML
    private Button buttonFeatures;
    @FXML
    private Button buttonServices;
    @FXML
    private Button buttonCustomers;
    @FXML
    private Button reservationsServicesButton;
    @FXML
    private Button mainPage;
    @FXML
    private Button buttonDeleteReservationsService;
    @FXML
    private Button buttonNewReservationsService;
    @FXML
    private Button buttonEditReservationsService;
    @FXML
    private TableView<ReservationServices> reservationsServicesTableView;

    @FXML
    private TableColumn<ReservationServices, Integer> columnReservationServiceId;
    @FXML
    private TableColumn<ReservationServices, Integer> columnReservationId;
    @FXML
    private TableColumn<ReservationServices, Integer> columnServiceId;
    @FXML
    private TableColumn<ReservationServices, String> columnServiceName;
    @FXML
    private TableColumn<ReservationServices, Double> columnUnitPrice;
    @FXML
    private TableColumn<ReservationServices, Integer> columnQuantity;
    @FXML
    private TableColumn<ReservationServices, Double> columnTotalPrice;
    @FXML
    TextField tf_searchReservationServiceByName;
    @FXML
    private Button logOut;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeButtons();
        populateReservationServicesTable();
        initializeTableColumns();


    }

    private void initializeButtons() {
        buttonRooms.setOnAction(e -> ScreenManager.showRoomsPage(buttonRooms.getScene()));
        buttonFeatures.setOnAction(e -> ScreenManager.showFeaturesPage(buttonFeatures.getScene()));
        buttonCustomers.setOnAction(e -> ScreenManager.showCustomersPage(buttonCustomers.getScene()));
        buttonServices.setOnAction(e -> ScreenManager.showServicesPage(buttonServices.getScene()));
        reservationsServicesButton.setOnAction(e -> ScreenManager.showReservationsServicesPage(reservationsServicesButton.getScene()));
        mainPage.setOnAction(e -> ScreenManager.showMainPage(mainPage.getScene()));
        buttonDeleteReservationsService.setOnAction(e -> ScreenManager.showDeleteReservationServicePage(buttonDeleteReservationsService.getScene()));
        buttonEditReservationsService.setOnAction(e -> ScreenManager.showEditReservationServicePage(buttonEditReservationsService.getScene()));
        buttonNewReservationsService.setOnAction(e -> ScreenManager.showNewReservationServicePage(buttonNewReservationsService.getScene()));
        logOut.setOnAction(e -> ScreenManager.showLogOut(logOut.getScene()));

    }


    private void initializeTableColumns() {

        columnReservationServiceId.setCellValueFactory(new PropertyValueFactory<>("reservationServiceId"));
        columnReservationId.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        columnServiceId.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        columnServiceName.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        columnUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        columnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        columnTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

    }

    private void populateReservationServicesTable() {
        List<ReservationServices> reservationServices = DBUtils.getAllReservationsServices();
        if (reservationServices != null) {
            ObservableList<ReservationServices> observableRooms = FXCollections.observableArrayList(reservationServices);
            reservationsServicesTableView.setItems(observableRooms);
        } else {
            System.out.println("An error occurred while retrieving rooms from the database.");
        }
    }


    public void handleSearchReservationServicesByServiceNameButtonClick(ActionEvent event) {
        String serviceName = tf_searchReservationServiceByName.getText();
        if (serviceName != null) {
            List<ReservationServices> filteredReservationServices = getReservationServicesByServiceName(serviceName);
            ObservableList<ReservationServices> observableList = FXCollections.observableArrayList(filteredReservationServices);
            reservationsServicesTableView.setItems(observableList);
        } else DBUtils.showErrorAlert("Error", "Service not found", "Please enter a valid Service name.");
    }

    private List<ReservationServices> getReservationServicesByServiceName(String serviceName) {
        List<ReservationServices> reservationServices = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD); PreparedStatement statement = connection.prepareStatement("SELECT * FROM reservation_services WHERE serviceName=?")) {
            statement.setString(1, serviceName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int reservationServiceId = resultSet.getInt("reservationsServicesId");
                    int reservationId = resultSet.getInt("reservationId");
                    int serviceId = resultSet.getInt("serviceId");
                    String serviceNameFromDb = resultSet.getString("serviceName");
                    double unitPrice = resultSet.getDouble("unitPrice");
                    int quantity = resultSet.getInt("quantity");
                    double totalPrice = resultSet.getDouble("totalPrice");
                    ReservationServices reservationService = new ReservationServices(reservationServiceId, reservationId, serviceId, serviceNameFromDb, unitPrice, quantity, totalPrice);
                    reservationServices.add(reservationService);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservationServices;
    }
}
