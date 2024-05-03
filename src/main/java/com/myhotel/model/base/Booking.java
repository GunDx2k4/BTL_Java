package com.myhotel.model.base;

import com.myhotel.Main;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Booking implements Serializable {
    private String bookingID;
    private String customerID;
    private String roomID;
    private Date checkInDate;
    private Date checkOutDate;

    public Booking() {

    }

    public Booking(String bookingID, String customerID, String roomID, Date checkInDate, Date checkOutDate) {
        this.bookingID = bookingID;
        this.customerID = customerID;
        this.roomID = roomID;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public long getTotalDay() {
        long day = TimeUnit.DAYS.convert((Math.abs(checkOutDate.getTime() - checkInDate.getTime())), TimeUnit.MILLISECONDS);
        return day + 1;
    }

    public float getTotalAmount() {
        return getTotalDay() * Main.getRoomByID(roomID).getPricePerNight();
    }
}
