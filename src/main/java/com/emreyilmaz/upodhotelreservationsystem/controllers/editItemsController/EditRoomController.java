package com.emreyilmaz.upodhotelreservationsystem.controllers.editItemsController;


import com.emreyilmaz.upodhotelreservationsystem.DBUtils;
import com.emreyilmaz.upodhotelreservationsystem.Room;
import com.emreyilmaz.upodhotelreservationsystem.ScreenManager;
import com.emreyilmaz.upodhotelreservationsystem.rooms.*;
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
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static com.emreyilmaz.upodhotelreservationsystem.DBUtils.*;


public class EditRoomController implements Initializable {
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
    private Button buttonNewRoom;
    @FXML
    private Button buttonEditRoom;
    @FXML
    private Button buttonDeleteRoom;


    @FXML
    private ComboBox<String> roomNameComboBox;

    @FXML
    private TextField roomNameTextField;

    @FXML
    private TextField roomCapacityTextField;

    @FXML
    private TextField roomPriceTextField;

    @FXML
    private TextField roomFeaturesTextField;
    @FXML
    private TextField totalRoomsTextField;
    @FXML
    private TextField availableRoomsTextField;
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


    public void handleEditRoomButtonClick(ActionEvent event) {
        String roomName = roomNameComboBox.getValue();
        String newRoomName = roomNameTextField.getText();
        String capacityString = roomCapacityTextField.getText();
        String priceString = roomPriceTextField.getText();
        String totalRoomsTF = totalRoomsTextField.getText();
        String availableRoomsTF = availableRoomsTextField.getText();


        try {
            int capacity = Integer.parseInt(capacityString);
            double price = Double.parseDouble(priceString);
            int totalRooms = Integer.parseInt(totalRoomsTF);
            int availableRooms = Integer.parseInt(availableRoomsTF);
            List<String> featuresList = getFeaturesFromTextField(roomFeaturesTextField);

            Room room = getRoomByName(roomName);
            if (room != null) {
                updateRoom(room, newRoomName, capacity, price, featuresList, totalRooms, availableRooms);
                setRoomFields(room);
                DBUtils.showSuccessAlert("Success", "Room Updated", "Room has been successfully updated.");
            } else {
                DBUtils.showErrorAlert("Error", "Room Not Found", "Room with name '" + roomName + "' was not found.");
            }

        } catch (NumberFormatException e) {
            DBUtils.showErrorAlert("Error", "Invalid Input", "Please enter valid numeric values for Capacity and Price.");
        }
    }

    private ObservableList<String> getAllRoomNames() {
        ObservableList<String> roomNames = FXCollections.observableArrayList();
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD)) {
            String sql = "SELECT roomName FROM rooms";
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

    private void setRoomFields(Room room) {
        roomNameTextField.setText(room.getRoomName());
        roomCapacityTextField.setText(String.valueOf(room.getCapacity()));
        roomPriceTextField.setText(String.valueOf(room.getPrice()));
        roomFeaturesTextField.setText(room.featuresToString());
        totalRoomsTextField.setText(String.valueOf(room.getTotalRooms()));
        availableRoomsTextField.setText(String.valueOf(room.getAvailableRooms()));

    }

    private void updateRoom(Room room, String newRoomName, int capacity, double price, List<String> features, int totalRooms, int availableRooms) {
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD)) {
            String sql = "UPDATE rooms SET RoomName=?, Capacity = ?, Price = ?, Features = ?,TotalRooms=?,AvailableRooms=? WHERE RoomId = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newRoomName);
                preparedStatement.setInt(2, capacity);
                preparedStatement.setDouble(3, price);
                preparedStatement.setString(4, features.toString());
                preparedStatement.setInt(5, totalRooms);
                preparedStatement.setInt(6, availableRooms);
                preparedStatement.setInt(7, room.getRoomId());
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Room updated successfully.");
                } else {
                    System.out.println("Room update failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Room getRoomByName(String roomType) {
        Room room = null;
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD)) {
            String sql = "SELECT * FROM rooms WHERE RoomName = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, roomType);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int roomId = resultSet.getInt("RoomId");
                        String roomName = resultSet.getString("RoomName");
                        int capacity = resultSet.getInt("Capacity");
                        double price = resultSet.getDouble("Price");
                        int totalRooms = resultSet.getInt("TotalRooms");
                        int availableRooms = resultSet.getInt("AvailableRooms");
                        List<String> features = DBUtils.getRoomFeaturesById(roomId);
                        switch (roomType) {
                            case "Single Room":
                                room = new SingleRoom(roomId, roomName, capacity, price, features, totalRooms, availableRooms);
                                break;
                            case "Double Room":
                                room = new DoubleRoom(roomId, roomName, capacity, price, features, totalRooms, availableRooms);
                                break;
                            case "Vip Room":
                                room = new VipRoom(roomId, roomName, capacity, price, features, totalRooms, availableRooms);
                                break;
                            case "Vip Dublex":
                                room = new VipDublex(roomId, roomName, capacity, price, features, totalRooms, availableRooms);
                                break;
                            case "King Suite":
                                room = new KingSuite(roomId, roomName, capacity, price, features, totalRooms, availableRooms);
                                break;

                            default:
                                room = new SingleRoom(roomId, roomName, capacity, price, features, totalRooms, availableRooms);
                                break;
                        }
                        room = new SingleRoom(roomId, roomName, capacity, price, features, totalRooms, availableRooms);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return room;
    }

    public static List<String> getFeaturesFromTextField(TextField textField) {
        String rawText = textField.getText();
        String[] featureArray = rawText.split(",");
        return Arrays.asList(featureArray);
    }
}
