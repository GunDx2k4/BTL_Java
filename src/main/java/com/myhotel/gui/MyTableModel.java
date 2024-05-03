package com.myhotel.gui;

import javax.swing.table.AbstractTableModel;
import java.util.Comparator;

public abstract class MyTableModel<T> extends AbstractTableModel {
    @Override
    public abstract int getRowCount();
    @Override
    public abstract int getColumnCount();
    @Override
    public abstract void setValueAt(Object value, int rowIndex, int columnIndex);
    @Override
    public abstract Object getValueAt(int rowIndex, int columnIndex);

    public abstract T getItem(int rowIndex);
    public abstract void sortItem(Comparator<T> comparator);
    public abstract void addItem(T obj);
    public abstract void removeItem(int rowIndex);
}
