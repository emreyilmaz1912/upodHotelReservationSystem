# Upod Hotel & Resort Reservation Manager System
# Emre Yılmaz
[![Watch the Demo Video](https://img.youtube.com/vi/4SonGaUNHoQ/0.jpg)](https://youtu.be/4SonGaUNHoQ)

## Overview

Upod Hotel & Resort Reservation Manager System is a JavaFX application for managing hotel reservations. The application allows users to add, edit, or delete customers, rooms, reservations, features, and reservation services. It also provides functionality to manage existing data, making it a comprehensive tool for hotel reservation management.

## Demo Video

Watch the application in action on YouTube: [Hotel Reservation Manager JavaFx](https://www.youtube.com/watch?v=4SonGaUNHoQ)
## Requirements

- Java SDK 20

## Features

- Add, edit, or delete customers
- Add, edit, or delete rooms
- Create new reservations
- Manage features offered by the hotel
- Handle reservation services
- Secure login with username and password

## Database Structure

### Table: customers

- customerId int AI PK
- fullName varchar(255)
- identityNumber varchar(20)
- phoneNumber varchar(15)
- birthDate date
- description text

### Table: features

- featureId int AI PK
- featureName varchar(255)

### Table: reservation_services

- reservationsServicesId int AI PK
- reservationId int
- serviceId int
- serviceName varchar(255)
- unitPrice double
- quantity int
- totalPrice double

### Table: reservations

- reservationId int AI PK
- roomId int
- checkInDate date
- checkOutDate date
- checkedInDate date
- checkedOutDate date
- customerId int
- room varchar(255)

### Table: rooms

- roomId int AI PK
- roomName varchar(255)
- capacity int
- price double
- features varchar(1000)
- totalRooms int
- availableRooms int

### Table: servicedetails

- serviceDetailId int AI PK
- serviceName varchar(255)
- servicePrice double
- quantity int
- totalServiceWage double

### Table: services

- serviceId int AI PK
- serviceName varchar(255)
- servicePrice double

### Table: managersusers

- managerId int AI PK
- username varchar(255)
- password varchar(255)

## Usage

1. Install Java SDK 20.
2. Run the application.
3. Log in with your username and password.

Feel free to explore and manage the hotel's reservations seamlessly with Upod Hotel & Resort Reservation Manager System!
