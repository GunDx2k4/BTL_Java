package com.myhotel.gui;

import com.myhotel.Main;
import com.myhotel.gui.table.*;
import com.myhotel.model.*;
import com.myhotel.model.base.Booking;
import com.myhotel.model.base.Room;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyFrame extends JFrame {
    private MyPanel<Customer> panelCustomer;
    private MyPanel<Staff> panelStaff;
    private MyPanel<Room> panelRoom;
    private MyPanel<Booking> panelBooking;
    private JRadioButton radioButtonStandardRoom;
    private JRadioButton radioButtonConnectingRoom;
    private JRadioButton radioButtonSuiteRoom;
    private ButtonGroup buttonGroup;
    private JLabel labelRoomSize;
    private MyTextField textRoomSize;
    private JLabel labelRooms;
    private MyComboBox<String> comboBoxRooms;
    private JLabel labelConnectingRoomID;
    private MyTextField textConnectingRoomID;
    private JButton buttonAddConnectingRoom;
    private JButton buttonDeleteConnectingRoom;
    private JCheckBox checkBoxHasLivingRoom;
    private JCheckBox checkBoxHasJacuzzi;
    private JCheckBox checkBoxHasMiniBar;

    public MyFrame(String title) {
        super(title);
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel labelHeader = new JLabel(title);
        labelHeader.setHorizontalAlignment(JLabel.CENTER);
        panel.add(labelHeader, BorderLayout.NORTH);

        setupPanelCustomer();

        setupPanelStaff();

        setupPanelRoom();

        setupPanelBooking();

        JTabbedPane tabbedPaneMain = new JTabbedPane();
        tabbedPaneMain.addTab("Khách hàng",panelCustomer);
        tabbedPaneMain.addTab("Nhân viên",panelStaff);
        tabbedPaneMain.addTab("Phòng",panelRoom);
        tabbedPaneMain.addTab("Đặt phòng",panelBooking);

        panel.add(tabbedPaneMain, BorderLayout.CENTER);
        add(panel);
    }

    private void setupPanelBooking() {
        panelBooking = new MyPanel<>("Mã đặt phòng", "Mã khách hàng", "Mã phòng", "Ngày nhận", "Ngày trả");
        panelBooking.setButtons();
        panelBooking.setButtonFind("Mã đặt phòng");
        panelBooking.setTable(new BookingTableModel());

        panelBooking.setButtonSort("Tổng tiền giảm dần", Main.BOOKING_TOTAL_COMPARATOR);

        panelBooking.setListSelectionListener();

        panelBooking.getButtonFind().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panelBooking.getTextFind().isEmpty()) {
                    return;
                }
                int index = Main.getIndexBookingByID(panelBooking.getTextFind().getText());
                if(index == -1) {
                    JOptionPane.showMessageDialog(null,"Không tìm thấy mã đặt phòng " + panelBooking.getTextFind().getText() + " !");
                    return;
                }
                for(int i = 0; i <  panelBooking.getTextFields().length; i++) {
                    panelBooking.getTextFields()[i].setText(panelBooking.getTableModel().getValueAt(index,i).toString());
                }
                panelBooking.getTable().getSelectionModel().setSelectionInterval(index,index);
            }
        });

        panelBooking.getButtonAdd().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(MyTextField textField : panelBooking.getTextFields()) {
                    if (textField.isEmpty()) {
                        return;
                    }
                }
                if(Main.getIndexBookingByID(panelBooking.getTextFields()[0].getText()) != -1) {
                    JOptionPane.showMessageDialog(null,"Đã có đơn đặt phòng mã " + panelBooking.getTextFields()[0].getText());
                    return;
                }
                Customer customer = Main.getCustomerByID(panelBooking.getTextFields()[1].getText());
                if(customer == null) {
                    JOptionPane.showMessageDialog(null,"Không tìm thấy khách hàng có mã " + panelBooking.getTextFields()[1].getText());
                    return;
                }
                if(Main.getRoomByID(panelBooking.getTextFields()[2].getText()) == null) {
                    JOptionPane.showMessageDialog(null,"Không tìm thấy phòng có mã " + panelBooking.getTextFields()[2].getText());
                    return;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date checkIn = null;
                Date checkOut = null;
                try {
                    checkIn = sdf.parse((panelBooking.getTextFields()[3].getText()));
                    checkOut = sdf.parse((panelBooking.getTextFields()[4].getText()));
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(null,"Vui lòng điền đúng dạng dd/MM/yyyy cho Ngày nhận và Ngày trả");
                    return;
                }

                if(Main.getRoomByID(panelBooking.getTextFields()[2].getText()) == null) {
                    JOptionPane.showMessageDialog(null,"Không tìm thấy phòng có mã " + panelBooking.getTextFields()[2].getText());
                    return;
                }
                if(checkIn.compareTo(checkOut) > 0) {
                    JOptionPane.showMessageDialog(null,"Ngày trả phải sau ngày nhận !");
                    return;
                }
                for(Booking booking : Main.BOOKINGS) {
                    if(booking.getRoomID().equals(panelBooking.getTextFields()[2].getText())) {
                        if(booking.getCheckInDate().compareTo(checkIn) >= 0 && booking.getCheckInDate().compareTo(checkOut) < 0) {
                            JOptionPane.showMessageDialog(null,String.format("%s đã có người đặt trong thời gian này", panelBooking.getTextFields()[2].getText()));
                            return;
                        }
                    }
                }

                customer.newBooking();
                Booking booking = new Booking();
                booking.setBookingID(panelBooking.getTextFields()[0].getText());
                panelBooking.getTextFields()[0].setText("");
                booking.setCustomerID(panelBooking.getTextFields()[1].getText());
                panelBooking.getTextFields()[1].setText("");
                booking.setRoomID(panelBooking.getTextFields()[2].getText());
                panelBooking.getTextFields()[2].setText("");
                booking.setCheckInDate(checkIn);
                panelBooking.getTextFields()[3].setText("");
                booking.setCheckOutDate(checkOut);
                panelBooking.getTextFields()[4].setText("");
                panelBooking.getTableModel().addItem(booking);
            }
        });

        panelBooking.getButtonDelete().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelBooking.getButtonDelete().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Customer customer = Main.getCustomerByID(panelBooking.getTableModel().getValueAt(panelBooking.getTable().getSelectedRow(),1).toString());
                        if(customer == null) {
                            JOptionPane.showMessageDialog(null,"Không tìm thấy khách hàng !");
                            return;
                        }
                        customer.setNumberOfBookings(customer.getNumberOfBookings() - 1);
                        if(panelBooking.getTable().getSelectedRow() < 0) {
                            return;
                        }
                        panelBooking.getTableModel().removeItem(panelBooking.getTable().getSelectedRow());
                    }
                });
            }
        });

        panelBooking.getButtonEdit().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(MyTextField textField : panelBooking.getTextFields()) {
                    if (textField.isEmpty()) {
                        return;
                    }
                }
                if(panelBooking.getTable().getSelectedRow() < 0) {
                    return;
                }
                Customer customer = Main.getCustomerByID(panelBooking.getTableModel().getValueAt(panelBooking.getTable().getSelectedRow(),1).toString());
                if(customer == null) {
                    JOptionPane.showMessageDialog(null,"Không tìm thấy khách hàng !");
                    return;
                }
                customer.setNumberOfBookings(customer.getNumberOfBookings() - 1);
                customer = Main.getCustomerByID(panelBooking.getTextFields()[1].getText());
                if(customer == null) {
                    JOptionPane.showMessageDialog(null,"Không tìm thấy khách hàng mới !");
                    return;
                }
                customer.newBooking();
                for(int i = 0; i < panelBooking.getTextFields().length; i++) {
                    panelBooking.getTableModel().setValueAt(panelBooking.getTextFields()[i].getText(),panelBooking.getTable().getSelectedRow(),i);
                    panelBooking.getTextFields()[i].setText("");
                }
            }
        });

        panelBooking.getButtonSave().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FileOutputStream fos = new FileOutputStream("booking.dat");
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(Main.BOOKINGS);
                    fos.close();
                    oos.close();

                    fos = new FileOutputStream("customer.dat");
                    oos = new ObjectOutputStream(fos);
                    oos.writeObject(Main.CUSTOMERS);
                    fos.close();
                    oos.close();
                    JOptionPane.showMessageDialog(null,String.format("Đã lưu thành công danh sách %s !","đơn đặt phòng"));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,String.format("Lưu danh sách %s không thành công !\n %s", "đơn đặt phòng", ex.getMessage()));
                }
            }
        });

        panelBooking.setLoadActionListener("booking.dat","đơn đặt phòng");
    }


    private void setupPanelStaff() {
        panelStaff = new MyPanel<>("Mã nhân viên", "Tên nhân viên", "SĐT nhân viên", "CCCD nhân viên", "Chức vụ nhân viên", "Hệ số lương");
        panelStaff.setButtons();
        panelStaff.setButtonFind("Mã nhân viên");
        panelStaff.setTable(new StaffTableModel());

        panelStaff.setButtonSort("Lương giảm dần", Main.STAFF_SALARY_COMPARATOR);

        panelStaff.setListSelectionListener();

        panelStaff.getButtonFind().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panelStaff.getTextFind().isEmpty()) {
                    return;
                }
                int index = Main.getIndexStaffByID(panelStaff.getTextFind().getText());
                if(index == -1) {
                    JOptionPane.showMessageDialog(null,"Không tìm thấy mã khách hàng " + panelBooking.getTextFind().getText() + " !");
                    return;
                }
                for(int i = 0; i <  panelStaff.getTextFields().length; i++) {
                    panelStaff.getTextFields()[i].setText(panelStaff.getTableModel().getValueAt(index,i).toString());
                }
                panelStaff.getTable().getSelectionModel().setSelectionInterval(index,index);
            }
        });

        panelStaff.getButtonAdd().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(MyTextField textField : panelStaff.getTextFields()) {
                    if (textField.isEmpty()) {
                        return;
                    }
                }
                if(Main.getIndexStaffByID(panelStaff.getTextFields()[0].getText()) != -1) {
                    JOptionPane.showMessageDialog(null,"Đã có nhân viên mã " + panelStaff.getTextFields()[0].getText());
                    return;
                }
                if(!panelStaff.getTextFields()[5].tryParseFloat()) {
                    JOptionPane.showMessageDialog(null,"Vui lòng điền đúng loại dữ liệu cho Hệ số lương");
                    return;
                }
                Staff staff = new Staff();
                staff.setStaffID(panelStaff.getTextFields()[0].getText());
                panelStaff.getTextFields()[0].setText("");
                staff.setName(panelStaff.getTextFields()[1].getText());
                panelStaff.getTextFields()[1].setText("");
                staff.setPhoneNumber(panelStaff.getTextFields()[2].getText());
                panelStaff.getTextFields()[2].setText("");
                staff.setCCCD(panelStaff.getTextFields()[3].getText());
                panelStaff.getTextFields()[3].setText("");
                staff.setPosition(panelStaff.getTextFields()[4].getText());
                panelStaff.getTextFields()[4].setText("");
                staff.setSalaryRatio(Float.parseFloat(panelStaff.getTextFields()[5].getText()));
                panelStaff.getTextFields()[5].setText("");
                panelStaff.getTableModel().addItem(staff);
            }
        });

        panelStaff.setDeleteActionListener();

        panelStaff.setEditActionListener();

        panelStaff.setSaveActionListener(Main.STAFFS,"staff.dat","nhân viên");

        panelStaff.setLoadActionListener("staff.dat","nhân viên");
    }
    private void setupPanelRoom() {
        panelRoom = new MyPanel<>("Mã phòng", "Tên phòng", "Loại giường", "Giá phòng", "Tầng");
        panelRoom.setButtons();
        panelRoom.setButtonFind("Mã phòng");
        panelRoom.setTable(new RoomTableModel());

        panelRoom.setButtonSort("Giá phòng giảm dần", Main.ROOM_PRICE_COMPARATOR);

        radioButtonStandardRoom = new JRadioButton("Phòng thường");
        radioButtonStandardRoom.setSelected(true);
        panelRoom.add(radioButtonStandardRoom);

        radioButtonConnectingRoom = new JRadioButton("Phòng kết nối");
        panelRoom.add(radioButtonConnectingRoom);

        radioButtonSuiteRoom = new JRadioButton("Phòng cao cấp");
        panelRoom.add(radioButtonSuiteRoom);

        buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonConnectingRoom);
        buttonGroup.add(radioButtonStandardRoom);
        buttonGroup.add(radioButtonSuiteRoom);

        panelRoom.getLayout().putConstraint(SpringLayout.WEST, radioButtonStandardRoom, 15, SpringLayout.EAST, panelRoom.getTextFields()[2]);
        panelRoom.getLayout().putConstraint(SpringLayout.BASELINE, radioButtonStandardRoom, 0, SpringLayout.BASELINE, panelRoom.getTextFields()[2]);
        panelRoom.getLayout().putConstraint(SpringLayout.WEST, radioButtonConnectingRoom, 15, SpringLayout.EAST, radioButtonStandardRoom);
        panelRoom.getLayout().putConstraint(SpringLayout.BASELINE, radioButtonConnectingRoom, 0, SpringLayout.BASELINE, radioButtonStandardRoom);
        panelRoom.getLayout().putConstraint(SpringLayout.WEST, radioButtonSuiteRoom, 15, SpringLayout.EAST, radioButtonConnectingRoom);
        panelRoom.getLayout().putConstraint(SpringLayout.BASELINE, radioButtonSuiteRoom, 0, SpringLayout.BASELINE, radioButtonConnectingRoom);

        labelRoomSize = new JLabel("Kích thước phòng");
        panelRoom.add(labelRoomSize);

        textRoomSize = new MyTextField(10);
        panelRoom.add(textRoomSize);

        panelRoom.getLayout().putConstraint(SpringLayout.WEST, labelRoomSize, 15, SpringLayout.EAST, panelRoom.getTextFields()[3]);
        panelRoom.getLayout().putConstraint(SpringLayout.BASELINE, labelRoomSize, 0, SpringLayout.BASELINE, panelRoom.getTextFields()[3]);
        panelRoom.getLayout().putConstraint(SpringLayout.WEST, textRoomSize, 15, SpringLayout.EAST, labelRoomSize);
        panelRoom.getLayout().putConstraint(SpringLayout.BASELINE, textRoomSize, 0, SpringLayout.BASELINE, labelRoomSize);

        labelRooms = new JLabel("Danh sách phòng kết nối");
        labelRooms.setVisible(false);
        panelRoom.add(labelRooms);

        comboBoxRooms = new MyComboBox<>();
        comboBoxRooms.setVisible(false);
        panelRoom.add(comboBoxRooms);

        labelConnectingRoomID = new JLabel("Mã phòng kết nối");
        labelConnectingRoomID.setVisible(false);
        panelRoom.add(labelConnectingRoomID);

        textConnectingRoomID = new MyTextField(5);
        textConnectingRoomID.setVisible(false);
        panelRoom.add(textConnectingRoomID);

        buttonDeleteConnectingRoom = new JButton("Xóa");
        buttonDeleteConnectingRoom.setVisible(false);
        panelRoom.add(buttonDeleteConnectingRoom);

        buttonAddConnectingRoom = new JButton("Thêm");
        buttonAddConnectingRoom.setVisible(false);
        panelRoom.add(buttonAddConnectingRoom);

        panelRoom.getLayout().putConstraint(SpringLayout.WEST, labelRooms, 15, SpringLayout.EAST, panelRoom.getTextFields()[3]);
        panelRoom.getLayout().putConstraint(SpringLayout.BASELINE, labelRooms, 0, SpringLayout.BASELINE, panelRoom.getTextFields()[3]);
        panelRoom.getLayout().putConstraint(SpringLayout.WEST, comboBoxRooms, 15, SpringLayout.EAST, labelRooms);
        panelRoom.getLayout().putConstraint(SpringLayout.BASELINE, comboBoxRooms, 0, SpringLayout.BASELINE, labelRooms);
        panelRoom.getLayout().putConstraint(SpringLayout.WEST, labelConnectingRoomID, 15, SpringLayout.EAST, panelRoom.getTextFields()[4]);
        panelRoom.getLayout().putConstraint(SpringLayout.BASELINE, labelConnectingRoomID, 0, SpringLayout.BASELINE, panelRoom.getTextFields()[4]);
        panelRoom.getLayout().putConstraint(SpringLayout.WEST, textConnectingRoomID, 15, SpringLayout.EAST, labelConnectingRoomID);
        panelRoom.getLayout().putConstraint(SpringLayout.BASELINE, textConnectingRoomID, 0, SpringLayout.BASELINE, labelConnectingRoomID);
        panelRoom.getLayout().putConstraint(SpringLayout.WEST, buttonAddConnectingRoom, 15, SpringLayout.EAST, textConnectingRoomID);
        panelRoom.getLayout().putConstraint(SpringLayout.BASELINE, buttonAddConnectingRoom, 0, SpringLayout.BASELINE, textConnectingRoomID);
        panelRoom.getLayout().putConstraint(SpringLayout.WEST, buttonDeleteConnectingRoom, 15, SpringLayout.EAST, buttonAddConnectingRoom);
        panelRoom.getLayout().putConstraint(SpringLayout.BASELINE, buttonDeleteConnectingRoom, 0, SpringLayout.BASELINE, buttonAddConnectingRoom);

        checkBoxHasLivingRoom = new JCheckBox("Có phòng khách");
        checkBoxHasLivingRoom.setVisible(false);
        panelRoom.add(checkBoxHasLivingRoom);

        checkBoxHasJacuzzi = new JCheckBox("Có bồn tắm sục");
        checkBoxHasJacuzzi.setVisible(false);
        panelRoom.add(checkBoxHasJacuzzi);

        checkBoxHasMiniBar = new JCheckBox("Có Minibar");
        checkBoxHasMiniBar.setVisible(false);
        panelRoom.add(checkBoxHasMiniBar);

        panelRoom.getLayout().putConstraint(SpringLayout.WEST, checkBoxHasLivingRoom, 15, SpringLayout.EAST, panelRoom.getTextFields()[3]);
        panelRoom.getLayout().putConstraint(SpringLayout.BASELINE, checkBoxHasLivingRoom, 0, SpringLayout.BASELINE, panelRoom.getTextFields()[3]);
        panelRoom.getLayout().putConstraint(SpringLayout.WEST, checkBoxHasJacuzzi, 15, SpringLayout.EAST, checkBoxHasLivingRoom);
        panelRoom.getLayout().putConstraint(SpringLayout.BASELINE, checkBoxHasJacuzzi, 0, SpringLayout.BASELINE, checkBoxHasLivingRoom);
        panelRoom.getLayout().putConstraint(SpringLayout.WEST, checkBoxHasMiniBar, 15, SpringLayout.EAST, checkBoxHasJacuzzi);
        panelRoom.getLayout().putConstraint(SpringLayout.BASELINE, checkBoxHasMiniBar, 0, SpringLayout.BASELINE, checkBoxHasJacuzzi);

        ItemListener listener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                ButtonModel selectedModel = buttonGroup.getSelection();
                if (selectedModel == radioButtonStandardRoom.getModel()) {
                    labelRoomSize.setVisible(true);
                    textRoomSize.setVisible(true);

                    labelRooms.setVisible(false);
                    comboBoxRooms.setVisible(false);
                    labelConnectingRoomID.setVisible(false);
                    textConnectingRoomID.setVisible(false);
                    buttonDeleteConnectingRoom.setVisible(false);
                    buttonAddConnectingRoom.setVisible(false);
                    checkBoxHasLivingRoom.setVisible(false);
                    checkBoxHasMiniBar.setVisible(false);
                    checkBoxHasJacuzzi.setVisible(false);
                    checkBoxHasLivingRoom.setSelected(false);
                } else if (selectedModel == radioButtonConnectingRoom.getModel()) {
                    labelRooms.setVisible(true);
                    comboBoxRooms.setVisible(true);
                    labelConnectingRoomID.setVisible(true);
                    textConnectingRoomID.setVisible(true);
                    buttonAddConnectingRoom.setVisible(true);
                    buttonDeleteConnectingRoom.setVisible(true);

                    labelRoomSize.setVisible(false);
                    textRoomSize.setVisible(false);
                    checkBoxHasLivingRoom.setVisible(false);
                    checkBoxHasMiniBar.setVisible(false);
                    checkBoxHasJacuzzi.setVisible(false);
                }else if (selectedModel == radioButtonSuiteRoom.getModel()) {
                    checkBoxHasLivingRoom.setVisible(true);
                    checkBoxHasMiniBar.setVisible(true);
                    checkBoxHasJacuzzi.setVisible(true);

                    labelRoomSize.setVisible(false);
                    textRoomSize.setVisible(false);
                    labelRooms.setVisible(false);
                    comboBoxRooms.setVisible(false);
                    labelConnectingRoomID.setVisible(false);
                    textConnectingRoomID.setVisible(false);
                    buttonAddConnectingRoom.setVisible(false);
                    buttonDeleteConnectingRoom.setVisible(false);
                }
            }
        };

        radioButtonStandardRoom.addItemListener(listener);
        radioButtonConnectingRoom.addItemListener(listener);
        radioButtonSuiteRoom.addItemListener(listener);

        buttonAddConnectingRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textConnectingRoomID.isEmpty()) {
                    return;
                }
                String roomID = textConnectingRoomID.getText();
                ConnectingRoom room = (ConnectingRoom)panelRoom.getTableModel().getItem(panelRoom.getTable().getSelectedRow());
                if(room == null) {
                    JOptionPane.showMessageDialog(null,"Tạo phòng mới trước rồi thêm phòng kết nối !");
                    return;
                }
                if(Main.getRoomByID(roomID) == null) {
                    JOptionPane.showMessageDialog(null,"Không tìm thấy phòng với mã " + roomID + " !");
                    return;
                };
                if(!(Main.getRoomByID(roomID) instanceof ConnectingRoom)) {
                    JOptionPane.showMessageDialog(null,"Phòng " + roomID + " không phải là phòng kết nối !");
                    return;
                };
                if(room.getRoomID().equals(roomID)) {
                    JOptionPane.showMessageDialog(null,"Không tự thêm phòng mình vào !");
                    return;
                };
                if(room.checkRoomConnecting(roomID)) {
                    JOptionPane.showMessageDialog(null,"Đã có phòng " + roomID +" trong danh sách phòng kết nối !");
                    return;
                };
                room.addRoomConnecting(roomID);
                ((ConnectingRoom)Main.getRoomByID(roomID)).addRoomConnecting(room.getRoomID());
                comboBoxRooms.addListItem(room.getRoomIDs());
                JOptionPane.showMessageDialog(null,"Đã thêm phòng " + roomID +" vào danh sách phòng kết nối thành công !");
                textConnectingRoomID.setText("");
            }
        });

        buttonDeleteConnectingRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textConnectingRoomID.isEmpty()) {
                    return;
                }
                String roomID = textConnectingRoomID.getText();
                ConnectingRoom room = (ConnectingRoom)panelRoom.getTableModel().getItem(panelRoom.getTable().getSelectedRow());
                ConnectingRoom connectingRoom = (ConnectingRoom)Main.getRoomByID(roomID);
                if(room == null) {
                    JOptionPane.showMessageDialog(null,"Tạo phòng mới trước rồi thêm phòng kết nối !");
                    return;
                }
                if(Main.getRoomByID(roomID) == null) {
                    JOptionPane.showMessageDialog(null,"Không tìm thấy phòng với mã " + roomID + " !");
                    return;
                };
                if(!(Main.getRoomByID(roomID) instanceof ConnectingRoom)) {
                    JOptionPane.showMessageDialog(null,"Phòng " + roomID + " không phải là phòng kết nối !");
                    return;
                };
                if(!room.checkRoomConnecting(roomID)) {
                    JOptionPane.showMessageDialog(null,"Không có phòng " + roomID +" trong danh sách phòng kết nối !");
                    return;
                };
                room.removeRoomConnecting(roomID);

                if(connectingRoom == null) {
                    JOptionPane.showMessageDialog(null,"Lỗi tìm phòng kết nối !");
                    return;
                }

                connectingRoom.removeRoomConnecting(room.getRoomID());
                comboBoxRooms.addListItem(room.getRoomIDs());
                JOptionPane.showMessageDialog(null,"Đã xóa phòng " + roomID +" vào danh sách phòng kết nối thành công !");
                textConnectingRoomID.setText("");
            }
        });

        panelRoom.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = panelRoom.getTable().getSelectedRow();
                    if (selectedRow != -1) {
                        for(int i = 0; i < panelRoom.getTextFields().length; i++) {
                            panelRoom.getTextFields()[i].setText(panelRoom.getTableModel().getValueAt(selectedRow,i).toString());
                        }
                        if (panelRoom.getTableModel().getItem(selectedRow) instanceof StandardRoom room) {
                            radioButtonStandardRoom.setSelected(true);
                            textRoomSize.setText(String.valueOf(room.getRoomSize()));
                        } else if (panelRoom.getTableModel().getItem(selectedRow) instanceof ConnectingRoom room) {
                            radioButtonConnectingRoom.setSelected(true);
                            comboBoxRooms.addListItem(room.getRoomIDs());
                        }else if (panelRoom.getTableModel().getItem(selectedRow) instanceof SuiteRoom room) {
                            radioButtonSuiteRoom.setSelected(true);
                            checkBoxHasLivingRoom.setSelected(room.isHasLivingRoom());
                            checkBoxHasJacuzzi.setSelected(room.isHasJacuzzi());
                            checkBoxHasMiniBar.setSelected(room.isHasMiniBar());
                        }
                    }
                }
            }
        });

        panelRoom.getButtonFind().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panelRoom.getTextFind().isEmpty()) {
                    return;
                }
                int index = Main.getIndexRoomByID(panelRoom.getTextFind().getText());
                for(int i = 0; i <  panelRoom.getTextFields().length; i++) {
                    panelRoom.getTextFields()[i].setText(panelRoom.getTableModel().getValueAt(index,i).toString());
                }

                if(index == -1) {
                    JOptionPane.showMessageDialog(null,"Không tìm thấy mã phòng " + panelBooking.getTextFind().getText() + " !");
                    return;
                }

                panelRoom.getTable().getSelectionModel().setSelectionInterval(index,index);

                if (panelRoom.getTableModel().getItem(index) instanceof StandardRoom room) {
                    radioButtonStandardRoom.setSelected(true);
                    textRoomSize.setText(String.valueOf(room.getRoomSize()));
                } else if (panelRoom.getTableModel().getItem(index) instanceof ConnectingRoom room) {
                    radioButtonConnectingRoom.setSelected(true);
                    comboBoxRooms.addListItem(room.getRoomIDs());
                }else if (panelRoom.getTableModel().getItem(index) instanceof SuiteRoom room) {
                    radioButtonSuiteRoom.setSelected(true);
                    checkBoxHasLivingRoom.setSelected(room.isHasLivingRoom());
                    checkBoxHasJacuzzi.setSelected(room.isHasJacuzzi());
                    checkBoxHasMiniBar.setSelected(room.isHasMiniBar());
                }
            }
        });

        panelRoom.getButtonAdd().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(MyTextField textField : panelRoom.getTextFields()) {
                    if (textField.isEmpty()) {
                        return;
                    }
                }
                if(Main.getIndexRoomByID(panelRoom.getTextFields()[0].getText()) != -1) {
                    JOptionPane.showMessageDialog(null,"Đã có phòng mã " + panelRoom.getTextFields()[0].getText());
                    return;
                }
                ButtonModel selectedModel = buttonGroup.getSelection();
                Room room = null;
                if (selectedModel == radioButtonStandardRoom.getModel()) {
                    room = new StandardRoom();
                    if (textRoomSize.isEmpty()) {
                        return;
                    }
                    ((StandardRoom)room).setRoomSize(Float.parseFloat(textRoomSize.getText()));
                    textRoomSize.setText("");
                } else if (selectedModel == radioButtonConnectingRoom.getModel()) {
                    room = new ConnectingRoom();
                    textConnectingRoomID.setText("");
                }else if (selectedModel == radioButtonSuiteRoom.getModel()) {
                    room = new SuiteRoom();

                    ((SuiteRoom)room).setHasJacuzzi(checkBoxHasJacuzzi.isSelected());
                    checkBoxHasJacuzzi.setSelected(false);
                    ((SuiteRoom)room).setHasMiniBar(checkBoxHasMiniBar.isSelected());
                    checkBoxHasMiniBar.setSelected(false);
                    ((SuiteRoom)room).setHasLivingRoom(checkBoxHasLivingRoom.isSelected());
                    checkBoxHasLivingRoom.setSelected(false);
                }
                if(room == null) {
                    JOptionPane.showMessageDialog(null,"Không tạo được phòng mới !");
                    return;
                }
                room.setRoomID(panelRoom.getTextFields()[0].getText());
                panelRoom.getTextFields()[0].setText("");
                room.setRoomName(panelRoom.getTextFields()[1].getText());
                panelRoom.getTextFields()[1].setText("");
                room.setTypeOfBed(panelRoom.getTextFields()[2].getText());
                panelRoom.getTextFields()[2].setText("");
                room.setPricePerNight(Float.parseFloat(panelRoom.getTextFields()[3].getText()));
                panelRoom.getTextFields()[3].setText("");
                room.setFloor(Integer.parseInt(panelRoom.getTextFields()[4].getText()));
                panelRoom.getTextFields()[4].setText("");

                panelRoom.getTableModel().addItem(room);

            }
        });

        panelRoom.setDeleteActionListener();

        panelRoom.getButtonEdit().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(MyTextField textField : panelRoom.getTextFields()) {
                    if (textField.isEmpty()) {
                        return;
                    }
                }
                if(panelRoom.getTable().getSelectedRow() < 0) {
                    return;
                }

                for(int i = 0; i < panelRoom.getTextFields().length; i++) {
                    panelRoom.getTableModel().setValueAt(panelRoom.getTextFields()[i].getText(),panelRoom.getTable().getSelectedRow(),i);
                    panelRoom.getTextFields()[i].setText("");
                }
                ButtonModel selectedModel = buttonGroup.getSelection();
                Room room = panelRoom.getTableModel().getItem(panelRoom.getTable().getSelectedRow());
                if (selectedModel == radioButtonStandardRoom.getModel()) {
                    if (textRoomSize.isEmpty()) {
                        return;
                    }
                    ((StandardRoom)room).setRoomSize(Float.parseFloat(textRoomSize.getText()));
                    textRoomSize.setText("");
                } else if (selectedModel == radioButtonConnectingRoom.getModel()) {
                    ((ConnectingRoom)room).setRoomIDs(comboBoxRooms.getListItem());
                    comboBoxRooms.removeAllItems();
                    textConnectingRoomID.setText("");
                }else if (selectedModel == radioButtonSuiteRoom.getModel()) {
                    ((SuiteRoom)room).setHasJacuzzi(checkBoxHasJacuzzi.isSelected());
                    checkBoxHasJacuzzi.setSelected(false);
                    ((SuiteRoom)room).setHasMiniBar(checkBoxHasMiniBar.isSelected());
                    checkBoxHasMiniBar.setSelected(false);
                    ((SuiteRoom)room).setHasLivingRoom(checkBoxHasLivingRoom.isSelected());
                    checkBoxHasLivingRoom.setSelected(false);
                }
            }
        });
        panelRoom.setSaveActionListener(Main.ROOMS,"room.dat","phòng");

        panelRoom.setLoadActionListener("room.dat","phòng");
    }

    private void setupPanelCustomer() {
        panelCustomer = new MyPanel<>("Mã khách hàng", "Tên khách hàng", "SĐT khách hàng", "CCCD khách hàng");
        panelCustomer.setButtons();
        panelCustomer.setButtonFind("Mã khách hàng");
        panelCustomer.setTable(new CustomerTableModel());

        JButton buttonBill = new JButton("Hóa đơn");
        panelCustomer.add(buttonBill);

        panelCustomer.getLayout().putConstraint(SpringLayout.WEST, buttonBill, 15, SpringLayout.EAST, panelCustomer.getTextFields()[2]);
        panelCustomer.getLayout().putConstraint(SpringLayout.BASELINE, buttonBill, 0, SpringLayout.BASELINE, panelCustomer.getTextFields()[2]);


        panelCustomer.setButtonSort("Lần đặt phòng giảm dần", Main.CUSTOMER_NUMBEROFBOOKING_COMPARATOR);

        buttonBill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panelCustomer.getTable().getSelectedRow() == -1) {
                    return;
                }

                JDialog dialog = new JDialog(MyFrame.this, "HÓA ĐƠN KHÁCH HÀNG", true);
                dialog.setSize(600, 400);
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());

                Customer customer = panelCustomer.getTableModel().getItem(panelCustomer.getTable().getSelectedRow());
                JLabel labelHeader = new JLabel("HÓA ĐƠN KHÁCH HÀNG " + customer.getCustomerID());
                labelHeader.setHorizontalAlignment(JLabel.CENTER);
                panel.add(labelHeader, BorderLayout.NORTH);


                Object[][] bookings = new Object[customer.getNumberOfBookings()][6];

                int i = 0;

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                for (Booking booking : Main.BOOKINGS) {
                    if (booking.getCustomerID().equals(customer.getCustomerID())) {
                        Room room = Main.getRoomByID(booking.getRoomID());
                        if (room == null) {
                            JOptionPane.showMessageDialog(null,"Không tìm thấy phòng !");
                            return;
                        }
                        bookings[i][0] = booking.getRoomID();
                        bookings[i][1] = room.getPricePerNight();
                        bookings[i][2] = sdf.format(booking.getCheckInDate());
                        bookings[i][3] = sdf.format(booking.getCheckOutDate());
                        bookings[i][4] = booking.getTotalDay();
                        bookings[i][5] = String.format("%.1f",booking.getTotalAmount());
                        ++i;
                    }
                }

                JTable table = new JTable(bookings, new String[]{"Mã phòng", "Giá phòng", "Ngày nhận", "Ngày trả", "Tổng ngày", "Thành tiền"});
                JScrollPane scrollPane = new JScrollPane(table);
                panel.add(scrollPane, BorderLayout.CENTER);

                float total = 0;
                for(Object[] booking : bookings) {
                    total += Float.parseFloat(booking[5].toString());
                }

                JLabel labelInfo = new JLabel("<html>Thông tin khách hàng"
                        + "<br>Tên : " + customer.getName()
                        + "<br>SĐT : " + customer.getPhoneNumber()
                        + "<br>CCCD : " + customer.getCCCD()
                        + "<br>Tổng tiền : " + String.format("%.1f",total)
                        + "<html>");
                panel.add(labelInfo, BorderLayout.WEST);

                dialog.add(panel);
                dialog.setVisible(true);
            }
        });

        panelCustomer.setListSelectionListener();

        panelCustomer.getButtonFind().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panelCustomer.getTextFind().isEmpty()) {
                    return;
                }
                int index = Main.getIndexCustomerByID(panelCustomer.getTextFind().getText());

                if(index == -1) {
                    JOptionPane.showMessageDialog(null,"Không tìm thấy mã khách hàng " + panelBooking.getTextFind().getText() + " !");
                    return;
                }

                for(int i = 0; i <  panelCustomer.getTextFields().length; i++) {
                    panelCustomer.getTextFields()[i].setText(panelCustomer.getTableModel().getValueAt(index,i).toString());
                }
                panelCustomer.getTable().getSelectionModel().setSelectionInterval(index,index);
            }
        });

        panelCustomer.getButtonAdd().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(MyTextField textField : panelCustomer.getTextFields()) {
                    if (textField.isEmpty()) {
                        return;
                    }
                }
                if(Main.getIndexCustomerByID(panelCustomer.getTextFields()[0].getText()) != -1) {
                    JOptionPane.showMessageDialog(null,"Đã có khách hàng mã " + panelCustomer.getTextFields()[0].getText());
                    return;
                }
                Customer customer = new Customer();
                customer.setCustomerID(panelCustomer.getTextFields()[0].getText());
                panelCustomer.getTextFields()[0].setText("");
                customer.setName(panelCustomer.getTextFields()[1].getText());
                panelCustomer.getTextFields()[1].setText("");
                customer.setPhoneNumber(panelCustomer.getTextFields()[2].getText());
                panelCustomer.getTextFields()[2].setText("");
                customer.setCCCD(panelCustomer.getTextFields()[3].getText());
                panelCustomer.getTextFields()[3].setText("");
                customer.setCustomerType("Thường");

                panelCustomer.getTableModel().addItem(customer);
            }
        });

        panelCustomer.setDeleteActionListener();

        panelCustomer.setEditActionListener();

        panelCustomer.setSaveActionListener(Main.CUSTOMERS,"customer.dat","khách hàng");

        panelCustomer.setLoadActionListener("customer.dat","khách hàng");
    }

    public void showFrame() {
        setVisible(true);
    }
}
