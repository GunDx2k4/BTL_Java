package com.myhotel.gui.table;

import com.myhotel.Main;
import com.myhotel.gui.MyTableModel;
import com.myhotel.model.Customer;
import com.myhotel.model.base.Room;

import javax.swing.table.AbstractTableModel;
import java.util.Comparator;

public class RoomTableModel extends MyTableModel<Room> {
    private static final String[] COLUMN_NAMES = {"Mã phòng", "Tên phòng", "Loại giường", "Giá phòng", "Tầng", "Loại phòng"};

    @Override
    public void sortItem(Comparator<Room> comparator) {
        Main.ROOMS.sort(comparator);
        fireTableDataChanged();
    }

    @Override
    public void addItem(Room room) {
        Main.ROOMS.add(room);
        fireTableDataChanged();
    }

    @Override
    public void removeItem(int rowIndex) {
        Main.ROOMS.remove(rowIndex);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return Main.ROOMS.size();
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
    public Room getItem(int rowIndex) {
        return Main.ROOMS.get(rowIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Room room = Main.ROOMS.get(rowIndex);
        switch (columnIndex) {
            case -1:
                return room;
            case 0:
                return room.getRoomID();
            case 1:
                return room.getRoomName();
            case 2:
                return room.getTypeOfBed();
            case 3:
                return room.getPricePerNight();
            case 4:
                return room.getFloor();
            case 5:
                return room.getTypeRoom();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Room room = Main.ROOMS.get(rowIndex);
        switch (columnIndex) {
            case 0:
                room.setRoomID(value.toString());
                break;
            case 1:
                room.setRoomName(value.toString());
                break;
            case 2:
                room.setTypeOfBed(value.toString());
                break;
            case 3:
                room.setPricePerNight(Float.parseFloat(value.toString()));
                break;
            case 4:
                room.setFloor(Integer.parseInt(value.toString()));
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
