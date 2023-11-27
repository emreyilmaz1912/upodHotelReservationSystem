package com.emreyilmaz.upodhotelreservationsystem.controllers;


import com.emreyilmaz.upodhotelreservationsystem.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.*;

import static com.emreyilmaz.upodhotelreservationsystem.DBUtils.*;


public class MainController implements Initializable {
    private int id;
    private Room room;
    private LocalDate Checked_In;
    private Customers customer;
    @FXML
    private DatePicker filterDateFrom;
    @FXML
    private DatePicker filterDateTo;
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
    private TableView<Reservations> reservationsTableView;
    @FXML
    private TextField tf_searchByName;
 ;

    private ObservableList<Room> roomsList = FXCollections.observableArrayList();
    private ObservableList<Reservations> reservationsList = FXCollections.observableArrayList();
    private ObservableList<Customers> customersList = FXCollections.observableArrayList();
    private ObservableList<Services> servicesList = FXCollections.observableArrayList();
    private ObservableList<Features> featuresList = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Reservations, Integer> columnId;
    @FXML
    private TableColumn<Reservations, Integer> columnRoom;
    @FXML
    private TableColumn<Reservations, Date> columnCheckInDate;
    @FXML
    private TableColumn<Reservations, Date> columnCheckOutDate;
    @FXML
    private TableColumn<Reservations, Date> columnCheckedIn;
    @FXML
    private TableColumn<Reservations, Date> columnCheckedOut;
    @FXML
    private TableColumn<Reservations, String> columnCustomers;
    @FXML
    private Button buttonNewReservation;
    @FXML
    private Button buttonEditReservation;
    @FXML
    private Button buttonDeleteReservation;
    @FXML
    private Button logOut;

    public MainController(Room room, Customers customer, Button buttonRooms, Button buttonFeatures, Button buttonServices, Button buttonCustomers, Button buttonReservation) {
        this.room = room;
        this.customer = customer;
        this.buttonRooms = buttonRooms;
        this.buttonFeatures = buttonFeatures;
        this.buttonServices = buttonServices;
        this.buttonCustomers = buttonCustomers;
        this.buttonNewReservation = buttonReservation;

    }


    public MainController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Reservations> reservations = DBUtils.getAllReservations();
        ObservableList<Reservations> observableReservations = FXCollections.observableArrayList(reservations);
        reservationsTableView.setItems(observableReservations);
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnRoom.setCellValueFactory(new PropertyValueFactory<>("room"));
        columnCheckInDate.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        columnCheckOutDate.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
        columnCheckedIn.setCellValueFactory(new PropertyValueFactory<>("checkedIn"));
        columnCheckedOut.setCellValueFactory(new PropertyValueFactory<>("checkedOut"));
        columnCustomers.setCellValueFactory(cellData -> {
            int customerId = cellData.getValue().getCustomerId();
            String fullName = DBUtils.getCustomerNameById(customerId);
            return new SimpleStringProperty(fullName);
        });
        filterReservationsByDateRange();
        initializeButtons();
    }

    public void initialize() {
        TableColumn<Reservations, Integer> columnId = new TableColumn<>("ID");
        TableColumn<Reservations, Room> columnRoom = new TableColumn<>("Room");
        TableColumn<Reservations, LocalDateTime> columnCheckInDate = new TableColumn<>("Check-In Date");
        TableColumn<Reservations, LocalDateTime> columnCheckOutDate = new TableColumn<>("Check-Out Date");
        TableColumn<Reservations, LocalDateTime> columnCheckedIn = new TableColumn<>("Checked-In");
        TableColumn<Reservations, LocalDateTime> columnCheckedOut = new TableColumn<>("Check-Out");
        TableColumn<Reservations, Integer> columnCustomers = new TableColumn<>("Customer");

        List<Reservations> reservationsList = DBUtils.getAllReservations();
        ObservableList<Reservations> observableReservations = FXCollections.observableArrayList(reservationsList);
        reservationsTableView.setItems(observableReservations);

    }

    private void initializeButtons() {
        buttonRooms.setOnAction(e -> ScreenManager.showRoomsPage(buttonRooms.getScene()));
        buttonFeatures.setOnAction(e -> ScreenManager.showFeaturesPage(buttonFeatures.getScene()));
        buttonCustomers.setOnAction(e -> ScreenManager.showCustomersPage(buttonCustomers.getScene()));
        buttonServices.setOnAction(e -> ScreenManager.showServicesPage(buttonServices.getScene()));
        reservationsServicesButton.setOnAction(e -> ScreenManager.showReservationsServicesPage(reservationsServicesButton.getScene()));

        buttonNewReservation.setOnAction(e -> ScreenManager.showNewReservationPage(buttonNewReservation.getScene()));
        buttonEditReservation.setOnAction(e -> ScreenManager.showEditReservationPage(buttonEditReservation.getScene()));
        buttonDeleteReservation.setOnAction(e -> ScreenManager.showDeleteReservationPage(buttonDeleteReservation.getScene()));
        logOut.setOnAction(e -> ScreenManager.showLogOut(logOut.getScene()));
    }

    public void filterReservationsByDateRange() {
        LocalDate fromDate = filterDateFrom.getValue();
        LocalDate toDate = filterDateTo.getValue();

        if (fromDate != null && toDate != null) {
            List<Reservations> filteredReservations = getReservationsByDateRange(fromDate, toDate);
            ObservableList<Reservations> observableFilteredReservations = FXCollections.observableArrayList(filteredReservations);
            reservationsTableView.setItems(observableFilteredReservations);
        } else {
            List<Reservations> allReservations = DBUtils.getAllReservations();
            ObservableList<Reservations> observableAllReservations = FXCollections.observableArrayList(allReservations);
            reservationsTableView.setItems(observableAllReservations);

        }
    }

    public void handleSearchReservationsByDateButtonClick(ActionEvent event) {
        LocalDate fromDate = filterDateFrom.getValue();
        LocalDate toDate = filterDateTo.getValue();
        if (fromDate != null && toDate != null && !fromDate.isAfter(toDate)) {
            List<Reservations> filteredReservations = getReservationsByCheckedInDate(fromDate, toDate);
            ObservableList<Reservations> observableFilteredReservations = FXCollections.observableArrayList(filteredReservations);
            reservationsTableView.setItems(observableFilteredReservations);
        } else {
            DBUtils.showConfirmationAlert("Dikkat!!!", "Geçerli bir tarih aralığı giriniz!", "Bütün rezervasyonları görmektesiniz.");

        }
    }

    private List<Reservations> getReservationsByCheckedInDate(LocalDate fromDate, LocalDate toDate) {
        List<Reservations> allReservations = DBUtils.getAllReservations();
        List<Reservations> filteredReservations = new ArrayList<>();
        for (Reservations reservation : allReservations) {
            LocalDate checkedInDate = reservation.getCheckedIn();
            LocalDate checkedOutDate = reservation.getCheckedOut();
            if (checkedInDate != null && checkedOutDate != null && !checkedOutDate.isBefore(fromDate) && !checkedInDate.isAfter(toDate)) {
                filteredReservations.add(reservation);
            }
        }
        return filteredReservations;
    }

    public void handleSearchReservationsByNameButtonClick(ActionEvent event) {
        String customerName = tf_searchByName.getText();
        if (customerName != null && !customerName.trim().isEmpty()) {
            List<Reservations> filteredReservations = getReservationsListByName(customerName);
            ObservableList<Reservations> observableFilteredReservations = FXCollections.observableArrayList(filteredReservations);
            reservationsTableView.setItems(observableFilteredReservations);
        } else {
            DBUtils.showErrorAlert("Error!", "Customer not found", "Please enter a valid customer name.");
        }
    }

    private List<Reservations> getReservationsListByName(String customerName) {
        List<Reservations> reservations = new ArrayList<>();
        String customerIdFromDb = getCustomerIdByName(customerName);
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD); PreparedStatement statement = connection.prepareStatement("SELECT * FROM reservations WHERE customerid=? ")) {
            statement.setString(1, customerIdFromDb);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("reservationId");
                    String roomName = resultSet.getString("roomId");
                    LocalDate checkedIn = resultSet.getDate("CheckedInDate").toLocalDate();
                    LocalDate checkedOut = resultSet.getDate("CheckedOutDate").toLocalDate();
                    LocalDate checkInDate = resultSet.getDate("CheckInDate").toLocalDate();
                    LocalDate checkOutDate = resultSet.getDate("CheckOutDate").toLocalDate();
                    int customerId = resultSet.getInt("customerId");
                    Room room = DBUtils.getRoomById(id);
                    Reservations reservation = new Reservations(id, room, checkInDate, checkOutDate, checkedIn, checkedOut, customerId);
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    private String getCustomerIdByName(String customerName) {
        String customerId = null;
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD); PreparedStatement statement = connection.prepareStatement("SELECT CustomerId FROM customers WHERE FullName = ?")) {
            statement.setString(1, customerName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    customerId = resultSet.getString("CustomerId");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerId;
    }

    public static List<Reservations> getReservationsByDateRange(LocalDate fromDate, LocalDate toDate) {
        List<Reservations> reservations = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION,USERNAME,PASSWORD); PreparedStatement statement = connection.prepareStatement("SELECT * FROM reservations WHERE Checked_In BETWEEN ? AND ?")) {
            statement.setDate(1, java.sql.Date.valueOf(fromDate));
            statement.setDate(2, java.sql.Date.valueOf(toDate));

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("reservationId");
                    int roomId = resultSet.getInt("RoomId");
                    LocalDate checkedIn = resultSet.getDate("CheckedIn").toLocalDate();
                    LocalDate checkedOut = resultSet.getDate("CheckedOut").toLocalDate();
                    LocalDate checkInDate = resultSet.getDate("CheckInDate").toLocalDate();
                    LocalDate checkOutDate = resultSet.getDate("CheckOutDate").toLocalDate();
                    int customerId = resultSet.getInt("CustomerId");

                    Room room = DBUtils.getRoomById(roomId);

                    Reservations reservation = new Reservations(id, room, checkInDate, checkOutDate, checkedIn, checkedOut, customerId);
                    reservations.add(reservation);
                }
            }

            return reservations;

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDate getChecked_In() {
        return Checked_In;
    }

    public void setChecked_In(LocalDate checked_In) {
        Checked_In = checked_In;
    }

    public Customers getCustomer() {
        return customer;
    }

    public void setCustomer(Customers customer) {
        this.customer = customer;
    }

    public void setFeaturesButtonAction(Runnable action) {
        buttonFeatures.setOnAction(e -> action.run());
    }

    public void setRoomsButtonAction(Runnable action) {
        buttonRooms.setOnAction(e -> action.run());
    }

    public void setCustomersButtonAction(Runnable action) {
        buttonCustomers.setOnAction(e -> action.run());
    }

    public void setServicesButtonAction(Runnable action) {
        buttonServices.setOnAction(e -> action.run());
    }

    public void setButtonEditAction(Runnable action) {
        buttonEditReservation.setOnAction(e -> action.run());
    }

    public void setButtonReservationAction(Runnable action) {
        buttonNewReservation.setOnAction(e -> action.run());
    }

    public void setButtonDeleteAction(Runnable action) {
        buttonDeleteReservation.setOnAction(e -> action.run());
    }

    public void setReservationsServicesButton(Runnable action) {
        reservationsServicesButton.setOnAction(e -> action.run());
    }

    public DatePicker getFilterDateFrom() {
        return filterDateFrom;
    }

    public void setFilterDateFrom(DatePicker filterDateFrom) {
        this.filterDateFrom = filterDateFrom;
    }

    public DatePicker getFilterDateTo() {
        return filterDateTo;
    }

    public void setFilterDateTo(DatePicker filterDateTo) {
        this.filterDateTo = filterDateTo;
    }

    public Button getButtonRooms() {
        return buttonRooms;
    }

    public void setButtonRooms(Button buttonRooms) {
        this.buttonRooms = buttonRooms;
    }

    public Button getButtonFeatures() {
        return buttonFeatures;
    }

    public void setButtonFeatures(Button buttonFeatures) {
        this.buttonFeatures = buttonFeatures;
    }

    public Button getButtonServices() {
        return buttonServices;
    }

    public void setButtonServices(Button buttonServices) {
        this.buttonServices = buttonServices;
    }

    public Button getButtonCustomers() {
        return buttonCustomers;
    }

    public void setButtonCustomers(Button buttonCustomers) {
        this.buttonCustomers = buttonCustomers;
    }

    public TableView<Reservations> getReservationsTableView() {
        return reservationsTableView;
    }

    public void setReservationsTableView(TableView<Reservations> reservationsTableView) {
        this.reservationsTableView = reservationsTableView;
    }

    public ObservableList<Room> getRoomsList() {
        return roomsList;
    }

    public void setRoomsList(ObservableList<Room> roomsList) {
        this.roomsList = roomsList;
    }

    public ObservableList<Reservations> getReservationsList() {
        return reservationsList;
    }

    public void setReservationsList(ObservableList<Reservations> reservationsList) {
        this.reservationsList = reservationsList;
    }

    public ObservableList<Customers> getCustomersList() {
        return customersList;
    }

    public void setCustomersList(ObservableList<Customers> customersList) {
        this.customersList = customersList;
    }

    public ObservableList<Services> getServicesList() {
        return servicesList;
    }

    public void setServicesList(ObservableList<Services> servicesList) {
        this.servicesList = servicesList;
    }

    public ObservableList<Features> getFeaturesList() {
        return featuresList;
    }

    public void setFeaturesList(ObservableList<Features> featuresList) {
        this.featuresList = featuresList;
    }

    public TableColumn<Reservations, Integer> getColumnId() {
        return columnId;
    }

    public void setColumnId(TableColumn<Reservations, Integer> columnId) {
        this.columnId = columnId;
    }

    public TableColumn<Reservations, Integer> getColumnRoom() {
        return columnRoom;
    }

    public void setColumnRoom(TableColumn<Reservations, Integer> columnRoom) {
        this.columnRoom = columnRoom;
    }

    public TableColumn<Reservations, Date> getColumnCheckInDate() {
        return columnCheckInDate;
    }

    public void setColumnCheckInDate(TableColumn<Reservations, Date> columnCheckInDate) {
        this.columnCheckInDate = columnCheckInDate;
    }

    public TableColumn<Reservations, Date> getColumnCheckOutDate() {
        return columnCheckOutDate;
    }

    public void setColumnCheckOutDate(TableColumn<Reservations, Date> columnCheckOutDate) {
        this.columnCheckOutDate = columnCheckOutDate;
    }

    public TableColumn<Reservations, Date> getColumnCheckedIn() {
        return columnCheckedIn;
    }

    public void setColumnCheckedIn(TableColumn<Reservations, Date> columnCheckedIn) {
        this.columnCheckedIn = columnCheckedIn;
    }

    public TableColumn<Reservations, Date> getColumnCheckedOut() {
        return columnCheckedOut;
    }

    public void setColumnCheckedOut(TableColumn<Reservations, Date> columnCheckedOut) {
        this.columnCheckedOut = columnCheckedOut;
    }

    public TableColumn<Reservations, String> getColumnCustomers() {
        return columnCustomers;
    }

    public void setColumnCustomers(TableColumn<Reservations, String> columnCustomers) {
        this.columnCustomers = columnCustomers;
    }

    public Button getButtonNewReservation() {
        return buttonNewReservation;
    }

    public void setButtonNewReservation(Button buttonNewReservation) {
        this.buttonNewReservation = buttonNewReservation;
    }

    public Button getButtonEditReservation() {
        return buttonEditReservation;
    }

    public void setButtonEditReservation(Button buttonEditReservation) {
        this.buttonEditReservation = buttonEditReservation;
    }

    public Button getButtonDeleteReservation() {
        return buttonDeleteReservation;
    }

    public void setButtonDeleteReservation(Button buttonDeleteReservation) {
        this.buttonDeleteReservation = buttonDeleteReservation;
    }

}
