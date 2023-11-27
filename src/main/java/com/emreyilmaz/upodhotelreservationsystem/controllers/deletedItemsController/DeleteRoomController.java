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

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static com.emreyilmaz.upodhotelreservationsystem.DBUtils.*;


public class DeleteRoomController implements Initializable {
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
    private ComboBox<String> roomNameComboBox;
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
        initializeButtons();
        initializeComboBox();
    }

    private void initializeButtons() {
        buttonRooms.setOnAction(e -> ScreenManager.showRoomsPage(buttonRooms.getScene()));
        buttonFeatures.setOnAction(e -> ScreenManager.showFeaturesPage(buttonFeatures.getScene()));
        buttonCustomers.setOnAction(e -> ScreenManager.showCustomersPage(buttonCustomers.getScene()));
        buttonServices.setOnAction(e -> ScreenManager.showServicesPage(buttonServices.getScene()));
        reservationsServicesButton.setOnAction(e -> ScreenManager.showReservationsServicesPage(reservationsServicesButton.getScene()));
        buttonDeleteRoom.setOnAction(e -> ScreenManager.showDeleteRoomPage(buttonDeleteRoom.getScene()));
        buttonEditRoom.setOnAction(e -> ScreenManager.showEditRoomPage(buttonEditRoom.getScene()));
        buttonNewRoom.setOnAction(e -> ScreenManager.showNewRoomPage(buttonNewRoom.getScene()));
        mainPage.setOnAction(e -> ScreenManager.showMainPage(mainPage.getScene()));
        logOut.setOnAction(e -> ScreenManager.showLogOut(logOut.getScene()));
    }

    private void initializeComboBox() {
        ObservableList<String> roomNames = getAllRoomNames();
        roomNameComboBox.setItems(roomNames);
    }

    public void handleDeleteRoomButtonClick(ActionEvent event) {
        String roomName = roomNameComboBox.getValue();
        if (roomName != null && !roomName.isEmpty()) {
            boolean confirmation = DBUtils.showConfirmationAlert("Delete Confirmation", "Are you sure you want to delete the room?", "This action cannot be undone.");
            if (confirmation) {
                deleteRoom(roomName);
            }
        } else {
            DBUtils.showErrorAlert("Error", "No Room Selected", "Please select a room to delete.");
        }
    }

    private void deleteRoom(String roomName) {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD);
            String deleteRoomSql = "DELETE FROM rooms WHERE RoomName = ?";
            try (PreparedStatement deleteRoomStatement = connection.prepareStatement(deleteRoomSql);
            ) {
                deleteRoomStatement.setString(1, roomName);
                int rowsAffected = deleteRoomStatement.executeUpdate();
                if (rowsAffected > 0) {
                    DBUtils.showSuccessAlert("Success", "Room Deleted", "Room has been successfully deleted.");
                } else {
                    DBUtils.showErrorAlert("Error", "Room Not Found", "Room with name '" + roomName + "' was not found.");
                }
            }
        } catch (SQLException e) {
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
            e.printStackTrace();
        }
    }

    private ObservableList<String> getAllRoomNames() {
        ObservableList<String> roomNames = FXCollections.observableArrayList();
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD)) {
            String sql = "SELECT RoomName FROM rooms";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String roomName = resultSet.getString("roomName");
                        roomNames.add(roomName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomNames;
    }
}
