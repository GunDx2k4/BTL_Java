package com.myhotel.model;

import com.myhotel.model.base.Person;

import java.io.Serializable;

public class Customer extends Person implements Serializable {
    private String customerID;
    private String customerType;
    private int numberOfBookings = 0;

    public Customer() {

    }

    public Customer(String name, String phoneNumber, String CCCD, String customerID, String customerType, int numberOfBookings) {
        super(name, phoneNumber, CCCD);
        this.customerID = customerID;
        this.customerType = customerType;
        this.numberOfBookings = numberOfBookings;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public int getNumberOfBookings() {
        return numberOfBookings;
    }

    public void setNumberOfBookings(int numberOfBookings) {
        this.numberOfBookings = numberOfBookings <= 0 ? 0 : numberOfBookings;
    }

    public void newBooking() {
        if(numberOfBookings < 0) numberOfBookings = 0;
        numberOfBookings++;
        if(numberOfBookings >= 5 && customerType.equals("Thường")) {
            customerType = "VIP";
        }
    }
}
