package com.myhotel.gui.table;

import com.myhotel.Main;
import com.myhotel.gui.MyTableModel;
import com.myhotel.model.Customer;

import java.util.Comparator;

public class CustomerTableModel extends MyTableModel<Customer> {
    private static final String[] COLUMN_NAMES = {"Mã KH", "Tên KH", "Số điện thoại", "CCCD", "Loại khách hàng", "Số lần đặt"};

    @Override
    public void sortItem(Comparator<Customer> comparator) {
        Main.CUSTOMERS.sort(comparator);
        fireTableDataChanged();
    }

    @Override
    public void addItem(Customer customer) {
        Main.CUSTOMERS.add(customer);
        fireTableDataChanged();
    }

    @Override
    public void removeItem(int rowIndex) {
        Main.CUSTOMERS.remove(rowIndex);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return Main.CUSTOMERS.size();
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
        Customer customer = Main.CUSTOMERS.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return customer.getCustomerID();
            case 1:
                return customer.getName();
            case 2:
                return customer.getPhoneNumber();
            case 3:
                return customer.getCCCD();
            case 4:
                return customer.getCustomerType();
            case 5:
                return customer.getNumberOfBookings();
            default:
                return null;
        }
    }

    @Override
    public Customer getItem(int rowIndex) {
        return Main.CUSTOMERS.get(rowIndex);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Customer customer = Main.CUSTOMERS.get(rowIndex);
        switch (columnIndex) {
            case 0:
                customer.setCustomerID(value.toString());
                break;
            case 1:
                customer.setName(value.toString());
                break;
            case 2:
                customer.setPhoneNumber(value.toString());
                break;
            case 3:
                customer.setCCCD(value.toString());
                break;
            case 4:
                customer.setCustomerType(value.toString());
                break;
            case 5:
                customer.setNumberOfBookings(Integer.parseInt(value.toString()));
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
