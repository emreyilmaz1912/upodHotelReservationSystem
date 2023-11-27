package com.emreyilmaz.upodhotelreservationsystem.controllers.editItemsController;


import com.emreyilmaz.upodhotelreservationsystem.*;
import com.emreyilmaz.upodhotelreservationsystem.rooms.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static com.emreyilmaz.upodhotelreservationsystem.DBUtils.*;


public class EditReservationController implements Initializable {

    @FXML
    public ComboBox<Room> roomComboBox;
    @FXML
    public ComboBox<Customers> customersComboBox;
    @FXML
    TextField tf_searchingByReservationId;
    @FXML
    private TextField roomNameTextField;
    @FXML
    private TextField customerNameTextField;
    @FXML
    private DatePicker checkInDatePicker;
    @FXML
    private DatePicker checkOutDatePicker;
    @FXML
    private DatePicker checkedInPicker;
    @FXML
    private DatePicker checkedOutPicker;
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
    private Button buttonNewReservation;
    @FXML
    private Button buttonEditReservation;
    @FXML
    private Button buttonDeleteReservation;
    @FXML
    private Button reservationsServicesButton;
    @FXML
    private Button logOut;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateRoomAndCustomerComboBoxes();
        initializeButtons();

        roomNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            Room selectedRoom = findRoomByName(newValue);
            roomComboBox.setValue(selectedRoom);
        });

        customerNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            String customerText = customerNameTextField.getText();
            if (customerText != null && customerText.matches("\\d+:\\s*[\\p{L}]+\\s+[\\p{L}]+")) {
                String[] parts = customerText.split(":");
                int customerId = Integer.parseInt(parts[0].trim());

                Customers selectedCustomer = findCustomerById(customerId);
                if (selectedCustomer != null) {
                    customersComboBox.setValue(selectedCustomer);
                }
            }
        });
    }

    private void initializeButtons() {
        buttonRooms.setOnAction(e -> ScreenManager.showRoomsPage(buttonRooms.getScene()));
        buttonFeatures.setOnAction(e -> ScreenManager.showFeaturesPage(buttonFeatures.getScene()));
        buttonCustomers.setOnAction(e -> ScreenManager.showCustomersPage(buttonCustomers.getScene()));
        buttonServices.setOnAction(e -> ScreenManager.showServicesPage(buttonServices.getScene()));
        reservationsServicesButton.setOnAction(e -> ScreenManager.showReservationsServicesPage(reservationsServicesButton.getScene()));
        mainPage.setOnAction(e -> ScreenManager.showMainPage(mainPage.getScene()));
        buttonNewReservation.setOnAction(e -> ScreenManager.showNewReservationPage(buttonNewReservation.getScene()));
        buttonEditReservation.setOnAction(e -> ScreenManager.showEditReservationPage(buttonEditReservation.getScene()));
        buttonDeleteReservation.setOnAction(e -> ScreenManager.showDeleteReservationPage(buttonDeleteReservation.getScene()));
        logOut.setOnAction(e -> ScreenManager.showLogOut(logOut.getScene()));
    }

    private void populateRoomAndCustomerComboBoxes() {
        List<Room> rooms = DBUtils.getAllRooms();
        ObservableList<Room> roomObservableList = FXCollections.observableArrayList(rooms);
        roomComboBox.setItems(roomObservableList);

        List<Customers> customers = DBUtils.getAllCustomers();
        ObservableList<Customers> customersObservableList = FXCollections.observableArrayList(customers);
        customersComboBox.setItems(customersObservableList);
    }

    private Room findRoomByName(String roomName) {
        List<Room> allRooms = DBUtils.getAllRooms();
        for (Room room : allRooms) {
            if (room.getRoomName().equalsIgnoreCase(roomName)) {
                return room;
            }
        }
        return null;
    }

    private Customers findCustomerById(int customerId) {
        List<Customers> allCustomers = DBUtils.getAllCustomers();
        for (Customers customer : allCustomers) {
            if (customer.getCustomerId() == customerId) {
                return customer;
            }
        }
        return null;
    }


    @FXML
    private void handleUpdateReservationButtonClick(ActionEvent event) {
        Room selectedRoom = roomComboBox.getValue();
        LocalDate checkInDateValue = checkInDatePicker.getValue();
        LocalDate checkOutDateValue = checkOutDatePicker.getValue();
        LocalDate checkedInDateValue = checkedInPicker.getValue();
        LocalDate checkedOutDateValue = checkedOutPicker.getValue();
        String customerInfo = customerNameTextField.getText();


        int customerId;
        String fullName;
        if (customerInfo != null && customerInfo.matches("\\d+:\\s*[\\p{L}]+\\s+[\\p{L}]+")) {
            String[] parts = customerInfo.split(":");
            customerId = Integer.parseInt(parts[0].trim());
            fullName = parts[1].trim();
        } else {
            DBUtils.showErrorAlert("Error", "Invalid Customer Format", "Customer format should be in the form of 'ID: Full Name'.");
            return;
        }
        Customers selectedCustomer = new Customers(customerId, fullName);
        int reservationId = getReservationIdFromUserInput();
        Reservations existingReservation = getReservationById(reservationId);
        if (existingReservation != null) {
            existingReservation.setRoom(selectedRoom);
            existingReservation.setCheckInDate(checkInDateValue);
            existingReservation.setCheckOutDate(checkOutDateValue);
            existingReservation.setCheckedIn(checkedInDateValue);
            existingReservation.setCheckedOut(checkedOutDateValue);
            existingReservation.setCustomerId(customerId);

            boolean success = updateReservation(existingReservation);
            if (success) {
                DBUtils.showErrorAlert("Success", "Reservation Updated", "Reservation has been successfully updated.");
            } else {
                DBUtils.showErrorAlert("Error", "Update Failed", "Failed to update the reservation. Please try again.");
            }
        } else {
            DBUtils.showErrorAlert("Error", "Reservation Not Found", "Reservation with ID " + reservationId + " was not found.");
        }
    }

    public boolean updateReservation(Reservations existingReservation) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean success = false;
        try {
            connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD);
            String sql = "UPDATE reservations SET Room=?, CheckInDate=?, CheckOutDate=?, CheckedIn=?, CheckedOut=?, customerid=? WHERE Id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, existingReservation.getRoom().getRoomId());
            preparedStatement.setDate(2, Date.valueOf(existingReservation.getCheckInDate()));
            preparedStatement.setDate(3, Date.valueOf(existingReservation.getCheckOutDate()));
            preparedStatement.setDate(4, Date.valueOf(existingReservation.getCheckedIn()));
            preparedStatement.setDate(5, Date.valueOf(existingReservation.getCheckedOut()));
            preparedStatement.setInt(6, existingReservation.getCustomerId());
            preparedStatement.setInt(7, existingReservation.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }


    public Reservations getReservationById(int reservationId) {
        Reservations reservation = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD);
            String sql = "SELECT * FROM reservations WHERE reservationId=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, reservationId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("reservationId");
                int roomId = resultSet.getInt("roomId");
                String roomName = getRoomNameById(roomId); // Kullanılacak bir fonksiyon
                int customerId = resultSet.getInt("customerId");

                // Room nesnesini oluştur
                Room room;
                switch (roomName) {
                    case "Single Room":
                        room = new SingleRoom();
                        break;
                    case "Double Room":
                        room = new DoubleRoom();
                        break;
                    case "Vip Room":
                        room = new VipRoom();
                        break;
                    case "Vip Dublex":
                        room = new VipDublex();
                        break;
                    case "King Suite":
                        room = new KingSuite();
                        break;
                    default:
                        room = new SingleRoom();
                        break;
                }

                Customers customer = new Customers(customerId);
                LocalDate checkInDate = resultSet.getDate("checkInDate").toLocalDate();
                LocalDate checkOutDate = resultSet.getDate("checkOutDate").toLocalDate();
                LocalDate checkedIn = resultSet.getDate("checkedInDate").toLocalDate();
                LocalDate checkedOut = resultSet.getDate("checkedOutDate").toLocalDate();

                reservation = new Reservations(id, room, checkInDate, checkOutDate, checkedIn, checkedOut, customerId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return reservation;
    }

    // Bu metod, roomId'yi kullanarak Room adını alır
    private String getRoomNameById(int roomId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String roomName = "Single Room"; // Varsayılan değer

        try {
            connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD);
            String sql = "SELECT roomName FROM rooms WHERE roomId=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, roomId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                roomName = resultSet.getString("roomName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return roomName;
    }


    private int getReservationIdFromUserInput() {
        String userInput = tf_searchingByReservationId.getText().trim();

        try {
            return Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }
    }

}
