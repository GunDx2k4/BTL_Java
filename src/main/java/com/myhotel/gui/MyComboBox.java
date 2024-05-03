package com.myhotel.gui;

import com.myhotel.Main;
import com.myhotel.model.ConnectingRoom;
import com.myhotel.model.base.Room;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MyComboBox<E> extends JComboBox<E> {

    public MyComboBox() {
        super();
        setPreferredSize(new Dimension(100, getPreferredSize().height));
    }

    public void addListItem(ArrayList<E> list) {
        removeAllItems();
        for(E e : list) {
            addItem(e);
        }
    }

    public ArrayList<E> getListItem() {
        ArrayList<E> list = new ArrayList<>();
        for(int i = 0; i < getItemCount(); i++) {
            list.add(getItemAt(i));
        }
        return list;
    }
}
