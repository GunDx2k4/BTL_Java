package com.myhotel.gui;

import com.myhotel.model.ConnectingRoom;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;

public class MyPanel<T> extends JPanel {
    private MyTextField[] textFields;
    private JLabel[] labels;
    private SpringLayout layout;
    private JButton buttonAdd;
    private JButton buttonDelete;
    private JButton buttonEdit;
    private JButton buttonSave;
    private JButton buttonLoad;
    private JButton buttonSort;
    private MyTextField textFind;
    private JLabel labelFind;
    private JButton buttonFind;
    private JScrollPane scrollPane;
    private JTable table;
    private MyTableModel<T> tableModel;

    public MyPanel() {

    }

    public MyPanel(String... fields) {
        super();
        layout = new SpringLayout();
        setLayout(layout);

        setTextFields(fields);
    }

    public MyTextField getTextFind() {
        return textFind;
    }

    public JLabel getLabelFind() {
        return labelFind;
    }

    public JButton getButtonFind() {
        return buttonFind;
    }

    @Override
    public SpringLayout getLayout() {
        return layout;
    }

    public JLabel[] getLabels() {
        return labels;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public JTable getTable() {
        return table;
    }

    public MyTableModel<T> getTableModel() {
        return tableModel;
    }

    public MyTextField[] getTextFields() {
        return textFields;
    }

    public JButton getButtonAdd() {
        return buttonAdd;
    }

    public JButton getButtonDelete() {
        return buttonDelete;
    }

    public JButton getButtonEdit() {
        return buttonEdit;
    }

    public JButton getButtonLoad() {
        return buttonLoad;
    }

    public JButton getButtonSave() {
        return buttonSave;
    }

    public JButton getButtonSort() {
        return buttonSort;
    }

    public void setLoadActionListener(String fileName, String name) {
        buttonLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FileInputStream fis = new FileInputStream(fileName);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    ArrayList<T> Ts = (ArrayList<T>)ois.readObject();
                    fis.close();
                    ois.close();
                    for(T t : Ts) {
                        tableModel.addItem(t);
                    }
                    JOptionPane.showMessageDialog(null,String.format("Đã đọc thành công danh sách %s !",name));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,String.format("Đọc danh sách %s không thành công !\n %s", name, ex.getMessage()));
                }
            }
        });
    }

    public void setSaveActionListener(ArrayList<T> list, String fileName, String name) {
        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FileOutputStream fos = new FileOutputStream(fileName);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(list);
                    fos.close();
                    oos.close();
                    JOptionPane.showMessageDialog(null,String.format("Đã lưu thành công danh sách %s !",name));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,String.format("Lưu danh sách %s không thành công !\n %s", name, ex.getMessage()));
                }
            }
        });
    }

    public void setDeleteActionListener() {
        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(table.getSelectedRow() < 0) {
                    return;
                }
                tableModel.removeItem(table.getSelectedRow());
            }
        });
    }

    public void setEditActionListener() {
        buttonEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(MyTextField textField : textFields) {
                    if (textField.isEmpty()) {
                        return;
                    }
                }
                if(table.getSelectedRow() < 0) {
                    return;
                }

                for(int i = 0; i < textFields.length; i++) {
                    tableModel.setValueAt(textFields[i].getText(),table.getSelectedRow(),i);
                    textFields[i].setText("");
                }
            }
        });
    }

    public void setListSelectionListener() {
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        for(int i = 0; i < textFields.length; i++) {
                            textFields[i].setText(table.getValueAt(selectedRow,i).toString());
                        }
                    }
                }
            }
        });
    }

    public void setButtonFind(String text) {
        labelFind = new JLabel(text);
        add(labelFind);

        textFind = new MyTextField(5);
        add(textFind);

        buttonFind = new JButton("Tìm");
        add(buttonFind);

        layout.putConstraint(SpringLayout.WEST, labelFind, 15, SpringLayout.EAST, buttonLoad);
        layout.putConstraint(SpringLayout.BASELINE, labelFind, 0, SpringLayout.BASELINE, buttonLoad);
        layout.putConstraint(SpringLayout.WEST, textFind, 15, SpringLayout.EAST, labelFind);
        layout.putConstraint(SpringLayout.BASELINE, textFind, 0, SpringLayout.BASELINE, labelFind);
        layout.putConstraint(SpringLayout.WEST, buttonFind, 15, SpringLayout.EAST, textFind);
        layout.putConstraint(SpringLayout.BASELINE, buttonFind, 0, SpringLayout.BASELINE, textFind);

    }

    public void setButtonSort(String text, Comparator<T> comparator) {
        buttonSort = new JButton(text);
        add(buttonSort);

        layout.putConstraint(SpringLayout.WEST, buttonSort, 15, SpringLayout.EAST, buttonEdit);
        layout.putConstraint(SpringLayout.BASELINE, buttonSort, 0, SpringLayout.BASELINE, buttonEdit);

        buttonSort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.sortItem(comparator);
            }
        });
    }

    public void setTable(MyTableModel tableModel) {
        this.tableModel = tableModel;
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);
        layout.putConstraint(SpringLayout.NORTH, scrollPane, 15, SpringLayout.SOUTH, textFields[textFields.length - 1]);
        layout.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, this);
        add(scrollPane);
    }

    public void setButtons() {
        buttonAdd = new JButton("Thêm");
        add(buttonAdd);

        buttonDelete = new JButton("Xóa");
        add(buttonDelete);

        buttonEdit = new JButton("Sửa");
        add(buttonEdit);

        buttonSave = new JButton("Lưu file");
        add(buttonSave);

        buttonLoad = new JButton("Đọc file");
        add(buttonLoad);

        layout.putConstraint(SpringLayout.WEST, buttonAdd, 15, SpringLayout.EAST, textFields[0]);
        layout.putConstraint(SpringLayout.BASELINE, buttonAdd, 0, SpringLayout.BASELINE, textFields[0]);
        layout.putConstraint(SpringLayout.WEST, buttonDelete, 15, SpringLayout.EAST, buttonAdd);
        layout.putConstraint(SpringLayout.BASELINE, buttonDelete, 0, SpringLayout.BASELINE, buttonAdd);
        layout.putConstraint(SpringLayout.WEST, buttonEdit, 15, SpringLayout.EAST, buttonDelete);
        layout.putConstraint(SpringLayout.BASELINE, buttonEdit, 0, SpringLayout.BASELINE, buttonDelete);

        layout.putConstraint(SpringLayout.WEST, buttonSave, 15, SpringLayout.EAST, textFields[1]);
        layout.putConstraint(SpringLayout.BASELINE, buttonSave, 0, SpringLayout.BASELINE, textFields[1]);
        layout.putConstraint(SpringLayout.WEST, buttonLoad, 15, SpringLayout.EAST, buttonSave);
        layout.putConstraint(SpringLayout.BASELINE, buttonLoad, 0, SpringLayout.BASELINE, buttonSave);
    }

    private void setTextFields(String... fields) {
        textFields = new MyTextField[fields.length];
        labels = new JLabel[fields.length];
        for (int i = 0; i < fields.length; i++) {
            labels[i] = new JLabel(fields[i]);
            textFields[i] = new MyTextField(20);

            add(labels[i]);
            add(textFields[i]);

            if(i == 0) {
                layout.putConstraint(SpringLayout.WEST, textFields[i], 110, SpringLayout.WEST, this);
                layout.putConstraint(SpringLayout.NORTH, textFields[i], 15, SpringLayout.NORTH, this);
                layout.putConstraint(SpringLayout.EAST, labels[i], -5, SpringLayout.WEST, textFields[i]);
                layout.putConstraint(SpringLayout.BASELINE, labels[i], 0, SpringLayout.BASELINE, textFields[i]);
            }
            else {
                layout.putConstraint(SpringLayout.WEST, textFields[i], 110, SpringLayout.WEST, this);
                layout.putConstraint(SpringLayout.NORTH, textFields[i], 15, SpringLayout.SOUTH, textFields[i - 1]);
                layout.putConstraint(SpringLayout.EAST, labels[i], -5, SpringLayout.WEST, textFields[i]);
                layout.putConstraint(SpringLayout.BASELINE, labels[i], 0, SpringLayout.BASELINE, textFields[i]);
            }
        }

    }
}
