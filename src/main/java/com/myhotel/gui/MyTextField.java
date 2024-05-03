package com.myhotel.gui;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyTextField extends JTextField {
    public MyTextField() {
        super();
    }

    public MyTextField(int columns) {
        super(columns);
    }

    public ArrayList<String> splitText() {
        String[] arr = getText().trim().split(",");
        ArrayList<String> list = new ArrayList<>();
        for(String text : arr) {
            if(!text.trim().isEmpty()) {
                list.add(text);
            }
        }
        return list;
    }

    public boolean tryParseFloat() {
        try {
            Float.parseFloat(getText());
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public boolean isEmpty() {
        if (getText().isEmpty()) {
            JOptionPane.showMessageDialog(null,"Vui lòng điền đầy đủ thông tin !");
            return true;
        }
        return false;
    }
}
