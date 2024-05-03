package com.myhotel.gui.table;

import com.myhotel.Main;
import com.myhotel.gui.MyTableModel;
import com.myhotel.model.base.Booking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class BookingTableModel extends MyTableModel<Booking> {
    private static final String[] COLUMN_NAMES = {"Mã đặt phòng", "Mã KH", "Mã phòng", "Ngày nhận", "Ngày trả", "Tổng tiền"};

    @Override
    public void sortItem(Comparator<Booking> comparator) {
        Main.BOOKINGS.sort(comparator);
        fireTableDataChanged();
    }

    @Override
    public void addItem(Booking booking) {
        Main.BOOKINGS.add(booking);
        fireTableDataChanged();
    }

    @Override
    public void removeItem(int rowIndex) {
        Main.BOOKINGS.remove(rowIndex);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return Main.BOOKINGS.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Booking booking = Main.BOOKINGS.get(rowIndex);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = null;
        switch (columnIndex) {
            case 0:
                return booking.getBookingID();
            case 1:
                return booking.getCustomerID();
            case 2:
                return booking.getRoomID();
            case 3:
                formattedDate = sdf.format(booking.getCheckInDate());
                return formattedDate;
            case 4:
                formattedDate = sdf.format(booking.getCheckOutDate());
                return formattedDate;
            case 5:
                return String.format("%.1f",booking.getTotalAmount());
            default:
                return null;
        }
    }

    @Override
    public Booking getItem(int rowIndex) {
        return Main.BOOKINGS.get(rowIndex);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Booking booking = Main.BOOKINGS.get(rowIndex);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        switch (columnIndex) {
            case 0:
                booking.setBookingID(value.toString());
                break;
            case 1:
                booking.setCustomerID(value.toString());
                break;
            case 2:
                booking.setRoomID(value.toString());
                break;
            case 3:
                try {
                    booking.setCheckInDate(sdf.parse(value.toString()));
                } catch (ParseException e) {
                }
                break;
            case 4:
                try {
                    booking.setCheckOutDate(sdf.parse(value.toString()));
                } catch (ParseException e) {
                }
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
