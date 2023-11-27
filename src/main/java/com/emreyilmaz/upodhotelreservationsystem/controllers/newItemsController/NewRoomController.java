package com.emreyilmaz.upodhotelreservationsystem.controllers.newItemsController;


import com.emreyilmaz.upodhotelreservationsystem.DBUtils;
import com.emreyilmaz.upodhotelreservationsystem.ScreenManager;
import com.emreyilmaz.upodhotelreservationsystem.controllers.editItemsController.EditRoomController;
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
import java.util.List;
import java.util.ResourceBundle;

import static com.emreyilmaz.upodhotelreservationsystem.DBUtils.*;


public class NewRoomController implements Initializable {
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
    private TextField tf_totalRooms;
    @FXML
    private Button buttonNewRoom;
    @FXML
    private Button buttonEditRoom;
    @FXML
    private Button buttonDeleteRoom;
    @FXML
    private TextField roomNameTextField;
    @FXML
    private TextField roomCapacityTextField;
    @FXML
    private TextField roomPriceTextField;
    @FXML
    private TextField roomFeaturesTextField;
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
        buttonDeleteRoom.setOnAction(e -> ScreenManager.showDeleteRoomPage(buttonDeleteRoom.getScene()));
        buttonEditRoom.setOnAction(e -> ScreenManager.showEditRoomPage(buttonEditRoom.getScene()));
        buttonNewRoom.setOnAction(e -> ScreenManager.showNewRoomPage(buttonNewRoom.getScene()));
        mainPage.setOnAction(e -> ScreenManager.showMainPage(mainPage.getScene()));
        logOut.setOnAction(e -> ScreenManager.showLogOut(logOut.getScene()));
    }

    public void handleNewRoomButtonClick(ActionEvent event) {
        String roomName = roomNameTextField.getText();
        String capacityString = roomCapacityTextField.getText();
        String priceString = roomPriceTextField.getText();
        String totalRoomsTf = tf_totalRooms.getText();
        try {
            int capacity = Integer.parseInt(capacityString);
            double price = Double.parseDouble(priceString);
            int totalRooms = Integer.parseInt(totalRoomsTf);
            int availableRooms = totalRooms;
            List<String> featuresList = EditRoomController.getFeaturesFromTextField(roomFeaturesTextField);
            Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD);
            String sql = "INSERT INTO rooms (RoomName, Capacity, Price, Features,TotalRooms,AvailableRooms) VALUES (?, ?, ?, ?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, roomName);
            preparedStatement.setInt(2, capacity);
            preparedStatement.setDouble(3, price);
            preparedStatement.setString(4, String.join(", ", featuresList));
            preparedStatement.setInt(5, totalRooms);
            preparedStatement.setInt(6, availableRooms);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
            DBUtils.showSuccessAlert("Success", "Room Added", "Room has been successfully added to the database.");

        } catch (NumberFormatException e) {
            DBUtils.showErrorAlert("Error", "Invalid Input", "Please enter valid numeric values for Capacity and Price.");
        } catch (SQLException e) {
            DBUtils.showErrorAlert("Error", "Database Error", "Failed to add room to the database.");
            e.printStackTrace();
        }
    }
}
