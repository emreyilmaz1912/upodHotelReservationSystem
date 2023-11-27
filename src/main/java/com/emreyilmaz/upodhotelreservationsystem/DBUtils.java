package com.emreyilmaz.upodhotelreservationsystem;



import com.emreyilmaz.upodhotelreservationsystem.controllers.MainController;
import com.emreyilmaz.upodhotelreservationsystem.rooms.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;


public class DBUtils {

    public final static String DATABASE_CONNECTION ="jdbc:mysql://localhost:3306/hotelupodmanagersystem";
    public final static String USERNAME = "root";
    public final static String PASSWORD = "8320209";
        
  
    public static List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION, USERNAME,PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM rooms");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("RoomId");
                String roomName = resultSet.getString("RoomName");
                int capacity = resultSet.getInt("Capacity");
                double price = resultSet.getDouble("Price");
                int totalRooms = resultSet.getInt("TotalRooms");
                int availableRooms = resultSet.getInt("AvailableRooms");
                String featuresString = resultSet.getString("Features");
                List<String> features = Arrays.asList(featuresString.split(","));
                Room room = createRoom(id, roomName, capacity, price, features, totalRooms, availableRooms);
                rooms.add(room);
            }
            return rooms;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    public static Room createRoom(int id, String roomName, int capacity, double price, List<String> features, int totalRooms, int availableRooms) {
        try {
            String className = "com.emreyilmaz.upodhotelreservationsystem.rooms." + roomName.replace(" ", "");
            Class<?> roomClass = Class.forName(className);

            // Sınıfın kurucu metodunu kontrol et
            Constructor<?> constructor = roomClass.getConstructor(int.class, String.class, int.class, double.class, List.class, int.class, int.class);

            // Kurucu metodu kullanarak sınıfı oluştur
            Room room = (Room) constructor.newInstance(id, roomName, capacity, price, features, totalRooms, availableRooms);
            return room;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
            return new SingleRoom(id, roomName, capacity, price, features, totalRooms, availableRooms);
        }
    }

    public static List<Customers> getAllCustomers() {
        List<Customers> customers = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION, USERNAME,PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM customers");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int customerId = resultSet.getInt("CustomerId");
                String fullName = resultSet.getString("FullName");
                String identityNumber = resultSet.getString("IdentityNumber");
                String phoneNumber = resultSet.getString("PhoneNumber");
                Date birthDate = resultSet.getDate("BirthDate");
                String description = resultSet.getString("Description");
                Customers customer = new Customers(customerId, fullName, identityNumber, phoneNumber, birthDate, description);
                customers.add(customer);
            }
            return customers;
        } catch (SQLException e) {
            e.printStackTrace();
            showConfirmationAlert("No data", "Customer List is empty.", "There is not any customer in the database");
            return Collections.emptyList();

        }
    }

    public static List<Services> getAllServices() {
        List<Services> services = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION, USERNAME,PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT *FROM services");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("ServiceId");
                String serviceName = resultSet.getString("ServiceName");
                double servicePrice = resultSet.getDouble("ServicePrice");
                Services service = new Services(id, serviceName, servicePrice);
                services.add(service);
            }
            return services;
        } catch (SQLException e) {
            e.printStackTrace();
            showConfirmationAlert("No data", "Services List is empty.", "There is not any services in the database");
            return Collections.emptyList();
        }
    }

    public static List<Reservations> getAllReservations() {
        List<Reservations> reservations = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION, USERNAME,PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM reservations");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("reservationId");
                String roomName = resultSet.getString("room");
                Room room;
                switch (roomName) {
                    case "Single Room":
                        room = new SingleRoom();
                        break;
                    case "Double Room":
                        room = new DoubleRoom();
                        break;

                    case "Vip Dublex":
                        room = new VipDublex();
                        break;
                    case "Vip Room":
                        room = new VipRoom();
                        break;
                    case "King Suite":
                        room = new KingSuite();
                        break;

                    default:

                        room = new SingleRoom();
                        break;
                }
                room.setRoomName(roomName);
                LocalDate checkInDate = resultSet.getDate("CheckInDate").toLocalDate();
                LocalDate checkOutDate = resultSet.getDate("CheckOutDate").toLocalDate();
                LocalDate checkedInDate = resultSet.getDate("checkedInDate").toLocalDate();
                LocalDate checkedOutDate = resultSet.getDate("CheckedOutDate").toLocalDate();
                int customerId = resultSet.getInt("customerId");
                Reservations reservation = new Reservations(id, room, checkInDate, checkOutDate, checkedInDate, checkedOutDate, customerId);
                reservations.add(reservation);
            }
            return reservations;
        } catch (SQLException e) {
            e.printStackTrace();
            showConfirmationAlert("No data", "Reservations list is empty.", "There is not any reservations in the database");
            return Collections.emptyList();
        }
    }

    public static List<ReservationServices> getAllReservationsServices() {
        List<ReservationServices> reservationServices = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION, USERNAME,PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM reservation_services");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("reservationsServicesId");
                int reservationId = resultSet.getInt("reservationId");
                int serviceId = resultSet.getInt("serviceId");
                String serviceName = resultSet.getString("serviceName");
                double unitPrice = resultSet.getDouble("unitPrice");
                int quantity = resultSet.getInt("quantity");
                double totalPrice = resultSet.getDouble("totalPrice");
                ReservationServices reservationsService = new ReservationServices(id, reservationId, serviceId, serviceName, unitPrice, quantity, totalPrice);
                reservationServices.add(reservationsService);
            }

            return reservationServices;

        } catch (SQLException e) {
            e.printStackTrace();
            showConfirmationAlert("Error", "No data", "An error occurred while retrieving reservation services from the database.");
            return Collections.emptyList();
        }
    }

    public static Room getRoomById(int roomId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION, USERNAME,PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM rooms WHERE RoomId = ?");
        ) {
            statement.setInt(1, roomId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String roomName = resultSet.getString("RoomName");
                int capacity = resultSet.getInt("Capacity");
                double price = resultSet.getDouble("Price");
                String featuresString = resultSet.getString("Features");
                int totalRooms = resultSet.getInt("TotalRooms");
                int availableRooms = resultSet.getInt("AvailableRooms");
                String typeName = resultSet.getString("RoomName");
                List<String> features = Arrays.asList(featuresString.split(","));
                Room room = createRoom(roomId, roomName, capacity, price, features, totalRooms, availableRooms);
                return room;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        showConfirmationAlert("Not found", "No room found with this ID.", "There is not any reservations in the database,Please try another ID.");
        return null;
    }

    public static List<Features> getAllFeatures() {
        List<Features> features = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION, USERNAME,PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT *FROM features");
             ResultSet resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {
                int featureId = resultSet.getInt("FeatureId");
                String featureName = resultSet.getString("FeatureName");
                Features feature = new Features(featureId, featureName);
                features.add(feature);
            }
            return features;
        } catch (SQLException e) {
            e.printStackTrace();
            showConfirmationAlert("Not found", "No features found in this database.", "There is no feature in the database.");
            return Collections.emptyList();
        }
    }

    public static void addNewReservation(Reservations reservation) {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        ResultSet generatedKeys = null;
        String sql = "INSERT INTO reservations (Room, CheckInDate, CheckOutDate, CheckedIn, CheckedOut,customerid) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            connection = DriverManager.getConnection(DATABASE_CONNECTION, USERNAME,PASSWORD);
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, reservation.getRoom().getRoomName());
            preparedStatement.setDate(2, java.sql.Date.valueOf(reservation.getCheckInDate()));
            preparedStatement.setDate(3, java.sql.Date.valueOf(reservation.getCheckOutDate()));
            preparedStatement.setDate(4, java.sql.Date.valueOf(reservation.getCheckedIn()));
            preparedStatement.setDate(5, java.sql.Date.valueOf(reservation.getCheckedOut()));
            preparedStatement.setInt(6, reservation.getCustomerId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating reservation failed, no raws affected");
            }
            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                reservation.setId(generatedId);
            } else {
                throw new SQLException("Creating reservation failed, no ID obtained.");
            }
            showSuccessAlert("Success", "New Reservation", "New reservation has been successfully saved.");
            System.out.println("Oluşturuldu.");
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Unsuccessful", "Record registration error", "New reservation could not be created, please try again.");
            System.err.println("Hata oluştu.");
        } finally {
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
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
    }

    public static void deleteReservationServicesByReservationId(int reservationId) {
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION, USERNAME,PASSWORD);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM reservation_services WHERE ReservationId = ?")) {
            statement.setInt(1, reservationId);
            statement.executeUpdate();
            showConfirmationAlert("Success", "Reservation Service deleted.", "Reservation service has been successfully deleted.");
        } catch (SQLException e) {
            showErrorAlert("Unsuccessful", "Reservation Service couldn't deleted.", "Reservation service hasn't been successfully deleted.Please try again.");
            e.printStackTrace();
        }
    }

    public static boolean deleteReservationById(int reservationId) {
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION, USERNAME,PASSWORD);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM reservations WHERE Id = ?")) {

            statement.setInt(1, reservationId);
            deleteReservationServicesByReservationId(reservationId);
            int affectedRows = statement.executeUpdate();
            showConfirmationAlert("Success", "Reservation  deleted.", "Reservation has been successfully deleted.");

            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();

            showErrorAlert("Unsuccessful", "Reservation  couldn't deleted.", "Reservation  hasn't been successfully deleted.Please try again.");
            return false;
        }
    }


    public static List<Customers> getCustomersWithReservations() {
        List<Customers> customersWithReservations = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DATABASE_CONNECTION, USERNAME,PASSWORD);
            String sql = "SELECT DISTINCT c.* FROM customers c INNER JOIN reservations r ON c.customerId = r.customerid";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int customerId = resultSet.getInt("customerId");
                String fullName = resultSet.getString("fullName");
                Customers customer = new Customers(customerId, fullName);
                customersWithReservations.add(customer);
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

        return customersWithReservations;
    }


    public static String getCustomerNameById(int customerId) {
        String customerName = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(DATABASE_CONNECTION, USERNAME,PASSWORD);
            String sql = "SELECT fullName FROM customers WHERE customerId = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, customerId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                customerName = resultSet.getString("FullName");
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
        return customerName;
    }

    public static void showErrorAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public static void showSuccessAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public static boolean showConfirmationAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
        return true;

    }
    public static void updateRoomAvailableRooms(int roomId, int newAvailableRooms) {
        String sql = "UPDATE rooms SET availableRooms = ? WHERE roomId = ?";

        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION, USERNAME,PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, newAvailableRooms);
            statement.setInt(2, roomId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                System.out.println("Room update failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getRoomFeaturesById(int roomId) {
        List<String> features = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION, USERNAME,PASSWORD)) {
            String sql = "SELECT features FROM rooms WHERE roomId = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, roomId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String featureName = resultSet.getString("Features");
                        features.add(featureName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return features;
    }

    public static List<String> getRoomFeaturesByName(String roomName) {
        List<String> features = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION, USERNAME,PASSWORD)) {
            String sql = "SELECT features FROM rooms WHERE  roomName= ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, roomName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String feature = resultSet.getString("Features");
                        features.add(feature);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return features;
    }

    public  static  void changeScene(ActionEvent event, String fxmlFile, String username) throws IOException {
        Parent root = null;
        if(username != null){
            FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
            root = loader.load();
            MainController mainScreenController = loader.getController();


        }else {
            root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

    }
    public static void logInUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(DATABASE_CONNECTION,"root","8320209");


            preparedStatement = connection.prepareStatement("SELECT password FROM managersusers WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Provided credentials are incorrect!");
            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found in the database");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String retrievedPassword = resultSet.getString("password");
                    if (retrievedPassword.equals(password)) {
                        changeScene(event, "main-screen.fxml", username);
                    } else {
                        System.out.println("Password did not match!");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    }

}

