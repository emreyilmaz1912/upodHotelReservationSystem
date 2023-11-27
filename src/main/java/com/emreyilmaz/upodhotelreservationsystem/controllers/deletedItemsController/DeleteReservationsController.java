package com.emreyilmaz.upodhotelreservationsystem.controllers.deletedItemsController;


import com.emreyilmaz.upodhotelreservationsystem.Customers;
import com.emreyilmaz.upodhotelreservationsystem.DBUtils;
import com.emreyilmaz.upodhotelreservationsystem.Reservations;
import com.emreyilmaz.upodhotelreservationsystem.ScreenManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.emreyilmaz.upodhotelreservationsystem.DBUtils.*;


public class DeleteReservationsController implements Initializable {
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
    private Button deleteReservationButton;
    @FXML
    private Button buttonEditReservation;
    @FXML
    private Button buttonDeleteReservation;
    @FXML
    private Button reservationsServicesButton;
    @FXML
    TextField tf_searchingByReservationId;
    @FXML
    TextField tf_searchingByCustomerName;
    @FXML
    private ComboBox<Customers> customersComboBox;
    @FXML
    private ComboBox<Reservations> reservationsComboBox;
@FXML
private Button logOut;

    private ObservableList<Reservations> reservationsList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateReservationsAndCustomersComboBoxes();
        initializeButtons();

    }

    private void initializeButtons() {
        buttonRooms.setOnAction(e -> ScreenManager.showRoomsPage(buttonRooms.getScene()));
        buttonFeatures.setOnAction(e -> ScreenManager.showFeaturesPage(buttonFeatures.getScene()));
        buttonCustomers.setOnAction(e -> ScreenManager.showCustomersPage(buttonCustomers.getScene()));
        buttonServices.setOnAction(e -> ScreenManager.showServicesPage(buttonServices.getScene()));
        reservationsServicesButton.setOnAction(e -> ScreenManager.showReservationsServicesPage(reservationsServicesButton.getScene()));
        mainPage.setOnAction(e -> ScreenManager.showMainPage( mainPage.getScene()));
        buttonNewReservation.setOnAction(e -> ScreenManager.showNewReservationPage(buttonNewReservation.getScene()));
        buttonEditReservation.setOnAction(e -> ScreenManager.showEditReservationPage(buttonEditReservation.getScene()));
        buttonDeleteReservation.setOnAction(e -> ScreenManager.showDeleteReservationPage(buttonDeleteReservation.getScene()));
        logOut.setOnAction(e -> ScreenManager.showLogOut(logOut.getScene()));
    }

    public void handleDeleteReservationButtonClick() {
        String reservationIdText = tf_searchingByReservationId.getText();
        String customerName = tf_searchingByCustomerName.getText();
        if (!reservationIdText.isEmpty()) {
            try {
                int id = Integer.valueOf(tf_searchingByReservationId.getText());
                Reservations reservationToDeleteById = getReservationById(id);

                if (reservationToDeleteById != null) {
                    boolean deleteConfirmation = showDeleteConfirmationAlert();
                    if (deleteConfirmation) {
                        boolean success = DBUtils.deleteReservationById(id);
                        if (success) {
                            DBUtils.showErrorAlert("Success", "Reservation Deleted", "Reservation has been successfully deleted.");
                            DBUtils.updateRoomAvailableRooms(reservationToDeleteById.getRoom().getRoomId(), 1);
                        } else {
                            DBUtils.showErrorAlert("Error", "Deletion Failed", "Failed to delete the reservation. Please try again.");
                        }
                        return;
                    }
                } else {
                    DBUtils.showErrorAlert("Error", "Reservation Not Found", "Reservation with ID " + id + " was not found.");
                }
            } catch (NumberFormatException e) {
                DBUtils.showErrorAlert("Error", "Invalid Reservation ID", "Please enter a valid reservation ID.");
            }
        }
        if (!customerName.isEmpty()) {
            Reservations reservationToDeleteByName = getReservationByCustomerName(customerName);
            if (reservationToDeleteByName != null) {
                boolean deleteConfirmation = showDeleteConfirmationAlert();
                if (deleteConfirmation) {
                    boolean success = deleteReservation(reservationToDeleteByName);
                    if (success) {
                        DBUtils.showErrorAlert("Success", "Reservation Deleted", "Reservation has been successfully deleted.");
                    } else {
                        DBUtils.showErrorAlert("Error", "Deletion Failed", "Failed to delete the reservation. Please try again.");
                    }
                }
            } else {
                DBUtils.showErrorAlert("Error", "Reservation Not Found", "Reservation with ID " + customerName + " was not found.");
            }
        }
    }

public Reservations getReservationByCustomerName(String customerName) {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Reservations reservation = null;

    try {
        connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD);
        String sql = "SELECT * FROM reservations INNER JOIN customers ON reservations.customerId = customers.customerId WHERE fullName = ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, customerName);
        resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int id = resultSet.getInt("reservationId");
            int roomId = resultSet.getInt("roomId");
            LocalDate checkInDate = resultSet.getDate("checkInDate").toLocalDate();
            LocalDate checkOutDate = resultSet.getDate("checkOutDate").toLocalDate();
            LocalDate checkedIn = resultSet.getDate("checkedInDate").toLocalDate();
            LocalDate checkedOut = resultSet.getDate("checkedOutDate").toLocalDate();
            int customerId = resultSet.getInt("customerId");

            reservation = new Reservations(id, String.valueOf(roomId), checkInDate, checkOutDate, checkedIn, checkedOut, customerId);
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
    public boolean showDeleteConfirmationAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Delete Confirmation");
        alert.setContentText("Are you sure you want to delete this reservation?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public boolean deleteReservation(Reservations reservationToDelete) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean success = false;
        try {
            connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD);
            String sql = "DELETE FROM reservations WHERE reservationId =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, reservationToDelete.getId());

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


    private void populateReservationsAndCustomersComboBoxes() {
        reservationsComboBox.setConverter(new StringConverter<Reservations>() {
            @Override
            public String toString(Reservations reservation) {
                return reservation != null ? String.valueOf(reservation.getId()) : null;
            }

            @Override
            public Reservations fromString(String string) {
                if (string == null || string.trim().isEmpty()) {
                    return null;
                }
                int reservationId = Integer.parseInt(string);
                for (Reservations reservation : reservationsList) {
                    if (reservation.getId() == reservationId) {
                        return reservation;
                    }
                }
                return null;
            }
        });
        List<Reservations> allReservations = DBUtils.getAllReservations();
        ObservableList<Reservations> reservationOptions = FXCollections.observableArrayList(allReservations);
        reservationsComboBox.setItems(reservationOptions);
        reservationsComboBox.setConverter(new StringConverter<Reservations>() {
            @Override
            public String toString(Reservations reservation) {
                return reservation != null ? String.valueOf(reservation.getId()) : null;
            }

            @Override
            public Reservations fromString(String string) {
                return null;
            }
        });


        List<Customers> allCustomersWithReservations = DBUtils.getCustomersWithReservations();
        ObservableList<Customers> customerOptions = FXCollections.observableArrayList(allCustomersWithReservations);
        customersComboBox.setItems(customerOptions);

        customersComboBox.setConverter(new StringConverter<Customers>() {
            @Override
            public String toString(Customers customer) {
                return customer != null ? customer.getFullName() : null;
            }

            @Override
            public Customers fromString(String string) {
                return null;
            }
        });
    }

    private Reservations getReservationById(int Id) {
        List<Reservations> allReservations = DBUtils.getAllReservations();
        for (Reservations reservation : allReservations) {
            if (reservation.getId() == Id) {
                return reservation;
            }
        }
        return null;
    }



}
