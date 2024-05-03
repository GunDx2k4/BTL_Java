package com.myhotel.gui.table;

import com.myhotel.Main;
import com.myhotel.gui.MyTableModel;
import com.myhotel.model.Staff;
import com.myhotel.model.base.Room;

import java.util.Comparator;

public class StaffTableModel extends MyTableModel<Staff> {
    private static final String[] COLUMN_NAMES = {"Mã NV", "Tên NV", "Số điện thoại", "CCCD", "Chức vụ", "Hệ số lương", "Lương"};

    @Override
    public void sortItem(Comparator<Staff> comparator) {
        Main.STAFFS.sort(comparator);
        fireTableDataChanged();
    }

    @Override
    public void addItem(Staff staff) {
        Main.STAFFS.add(staff);
        fireTableDataChanged();
    }

    @Override
    public void removeItem(int rowIndex) {
        Main.STAFFS.remove(rowIndex);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return Main.STAFFS.size();
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
    public Staff getItem(int rowIndex) {
        return Main.STAFFS.get(rowIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Staff staff = Main.STAFFS.get(rowIndex);
        switch (columnIndex) {
            case -1:
                return staff;
            case 0:
                return staff.getStaffID();
            case 1:
                return staff.getName();
            case 2:
                return staff.getPhoneNumber();
            case 3:
                return staff.getCCCD();
            case 4:
                return staff.getPosition();
            case 5:
                return staff.getSalaryRatio();
            case 6:
                return staff.getSalary();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Staff staff = Main.STAFFS.get(rowIndex);
        switch (columnIndex) {
            case 0:
                staff.setStaffID(value.toString());
                break;
            case 1:
                staff.setName(value.toString());
                break;
            case 2:
                staff.setPhoneNumber(value.toString());
                break;
            case 3:
                staff.setCCCD(value.toString());
                break;
            case 4:
                staff.setPosition(value.toString());
                break;
            case 5:
                staff.setSalaryRatio(Float.parseFloat(value.toString()));
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
