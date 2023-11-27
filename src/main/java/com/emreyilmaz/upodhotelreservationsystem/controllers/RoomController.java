package com.emreyilmaz.upodhotelreservationsystem.controllers;


import com.emreyilmaz.upodhotelreservationsystem.DBUtils;
import com.emreyilmaz.upodhotelreservationsystem.Room;
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


public class RoomController implements Initializable {
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
    private TableView<Room> roomTableView;
    @FXML
    private TableColumn<Room, Integer> columnId;
    @FXML
    private TableColumn<Room, String> columnRoomName;
    @FXML
    private TableColumn<Room, Integer> columnCapacity;
    @FXML
    private TableColumn<Room, Double> columnPrice;
    @FXML
    private TableColumn<Room, List<String>> columnFeatures;
    @FXML
    private TableColumn<Room, String> columnTotalRooms;
    @FXML
    private TableColumn<Room, String> columnAvailableRooms;
    @FXML
    private Button buttonNewRoom;
    @FXML
    private Button buttonEditRoom;
    @FXML
    private Button buttonDeleteRoom;
    @FXML
    private Button logOut;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateRoomTable();
        initializeTableColumns();
        buttonRooms.setOnAction(e -> ScreenManager.showRoomsPage(buttonRooms.getScene()));
        buttonFeatures.setOnAction(e -> ScreenManager.showFeaturesPage(buttonFeatures.getScene()));
        buttonCustomers.setOnAction(e -> ScreenManager.showCustomersPage(buttonCustomers.getScene()));
        buttonServices.setOnAction(e -> ScreenManager.showServicesPage(buttonServices.getScene()));
        reservationsServicesButton.setOnAction(e -> ScreenManager.showReservationsServicesPage(reservationsServicesButton.getScene()));
        mainPage.setOnAction(e -> ScreenManager.showMainPage(mainPage.getScene()));
        buttonDeleteRoom.setOnAction(e -> ScreenManager.showDeleteRoomPage(buttonDeleteRoom.getScene()));
        buttonEditRoom.setOnAction(e -> ScreenManager.showEditRoomPage(buttonEditRoom.getScene()));
        buttonNewRoom.setOnAction(e -> ScreenManager.showNewRoomPage(buttonNewRoom.getScene()));
        logOut.setOnAction(e -> ScreenManager.showLogOut(logOut.getScene()));

    }

    private void initializeTableColumns() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        columnRoomName.setCellValueFactory(new PropertyValueFactory<>("roomName"));
        columnCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnFeatures.setCellValueFactory(new PropertyValueFactory<>("features"));
        columnTotalRooms.setCellValueFactory(new PropertyValueFactory<>("totalRooms"));
        columnAvailableRooms.setCellValueFactory(new PropertyValueFactory<>("availableRooms"));
    }


    private void populateRoomTable() {
        List<Room> rooms = DBUtils.getAllRooms();
        if (rooms != null) {
            ObservableList<Room> observableRooms = FXCollections.observableArrayList(rooms);
            roomTableView.setItems(observableRooms);
        } else {
            System.out.println("An error occurred while retrieving rooms from the database.");
        }
    }
}
