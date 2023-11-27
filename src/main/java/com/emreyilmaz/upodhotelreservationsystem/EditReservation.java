package com.emreyilmaz.upodhotelreservationsystem;



import com.emreyilmaz.upodhotelreservationsystem.Room;

import java.time.LocalDate;


public class EditReservation {
    private Room room;
    private int id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDate CheckedIn;
    private LocalDate CheckedOut;
    private int customerId;

    public EditReservation() {
    }

    public EditReservation(Room room, int id, LocalDate checkInDate, LocalDate checkOutDate, LocalDate checkedIn, LocalDate checkedOut, int customerId) {
        this.room = room;
        this.id = id;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        CheckedIn = checkedIn;
        CheckedOut = checkedOut;
        this.customerId = customerId;
    }
}
