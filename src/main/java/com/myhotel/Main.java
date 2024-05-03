package com.myhotel;

import com.myhotel.gui.MyFrame;
import com.myhotel.model.Customer;
import com.myhotel.model.Staff;
import com.myhotel.model.base.Booking;
import com.myhotel.model.base.Room;

import java.util.ArrayList;
import java.util.Comparator;

public class Main {
    public static ArrayList<Customer> CUSTOMERS = new ArrayList<>();
    public static ArrayList<Staff> STAFFS = new ArrayList<>();
    public static ArrayList<Room> ROOMS = new ArrayList<>();
    public static ArrayList<Booking> BOOKINGS = new ArrayList<>();
    public static final float SALARY_BASE = 1500000;
    public static final Comparator<Customer> CUSTOMER_NUMBEROFBOOKING_COMPARATOR = new Comparator<Customer>() {
        @Override
        public int compare(Customer o1, Customer o2) {
            if(o1.getNumberOfBookings() <= o2.getNumberOfBookings())
                return 1;
            else
                return -1;
        }
    };
    public static final Comparator<Staff> STAFF_SALARY_COMPARATOR = new Comparator<Staff>() {
        @Override
        public int compare(Staff o1, Staff o2) {
            if(o1.getSalary() <= o2.getSalary())
                return 1;
            else
                return -1;
        }
    };
    public static final Comparator<Room> ROOM_PRICE_COMPARATOR = new Comparator<Room>() {
        @Override
        public int compare(Room o1, Room o2) {
            if(o1.getPricePerNight() <= o2.getPricePerNight())
                return 1;
            else
                return -1;
        }
    };

    public static final Comparator<Booking> BOOKING_TOTAL_COMPARATOR = new Comparator<Booking>() {
        @Override
        public int compare(Booking o1, Booking o2) {
            if(o1.getTotalAmount() <= o2.getTotalAmount())
                return 1;
            else
                return -1;
        }
    };

    public static void main(String[] args) {
        MyFrame frameMain = new MyFrame("QUẢN LÝ KHÁCH SẠN");
        frameMain.showFrame();
    }

    public static int getIndexCustomerByID(String customerID) {
        for(Customer customer : CUSTOMERS) {
            if (customer.getCustomerID().equals(customerID)) {
                return CUSTOMERS.indexOf(customer);
            }
        }
        return -1;
    }

    public static int getIndexStaffByID(String staffID) {
        for(Staff staff : STAFFS) {
            if (staff.getStaffID().equals(staffID)) {
                return STAFFS.indexOf(staff);
            }
        }
        return -1;
    }

    public static int getIndexRoomByID(String roomID) {
        for(Room room : ROOMS) {
            if (room.getRoomID().equals(roomID)) {
                return ROOMS.indexOf(room);
            }
        }
        return -1;
    }

    public static int getIndexBookingByID(String bookingID) {
        for(Booking booking : BOOKINGS) {
            if (booking.getBookingID().equals(bookingID)) {
                return BOOKINGS.indexOf(booking);
            }
        }
        return -1;
    }

    public static Customer getCustomerByID(String customerID) {
        for(Customer customer : CUSTOMERS) {
            if (customer.getCustomerID().equals(customerID)) {
                return customer;
            }
        }
        return null;
    }

    public static Staff getStaffByID(String staffID) {
        for(Staff staff : STAFFS) {
            if (staff.getStaffID().equals(staffID)) {
                return staff;
            }
        }
        return null;
    }

    public static Room getRoomByID(String roomID) {
        for(Room room : ROOMS) {
            if (room.getRoomID().equals(roomID)) {
                return room;
            }
        }
        return null;
    }

    public static Booking getBookingByID(String bookingID) {
        for(Booking booking : BOOKINGS) {
            if (booking.getBookingID().equals(bookingID)) {
                return booking;
            }
        }
        return null;
    }
}