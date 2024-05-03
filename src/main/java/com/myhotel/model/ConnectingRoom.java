package com.myhotel.model;

import com.myhotel.Main;
import com.myhotel.model.base.Room;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;

public class ConnectingRoom extends Room implements Serializable {
    private ArrayList<String> roomIDs;

    public ConnectingRoom() {
        roomIDs = new ArrayList<>();
    }

    public ConnectingRoom(String roomID, String roomName, String typeOfBed, float pricePerNight, int floor, ArrayList<String> roomIDs) {
        super(roomID, roomName, typeOfBed, pricePerNight, floor);
        this.roomIDs = roomIDs;
    }

    public ArrayList<String> getRoomIDs() {
        return roomIDs;
    }

    public void setRoomIDs(ArrayList<String> roomIDs) {
        this.roomIDs = roomIDs;
    }

    public void addRoomConnecting(String roomID) {
        roomIDs.add(roomID);
    }

    public void removeRoomConnecting(String roomConnectingID) {
        roomIDs.remove(roomConnectingID);
    }

    public boolean checkRoomConnecting(String roomConnectingID) {
        for (String roomID : roomIDs) {
            if(roomID.equals(roomConnectingID)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getTypeRoom() {
        return "Phòng kết nối";
    }
}
