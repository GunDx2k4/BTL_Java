package com.myhotel.model;

import com.myhotel.model.base.Room;

import java.io.Serializable;

public class StandardRoom extends Room implements Serializable {
    private float roomSize;

    public StandardRoom() {

    }

    public StandardRoom(String roomID, String roomName, String typeOfBed, float pricePerNight, int floor, float roomSize) {
        super(roomID, roomName, typeOfBed, pricePerNight, floor);
        this.roomSize = roomSize;
    }

    public float getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(float roomSize) {
        this.roomSize = roomSize;
    }

    @Override
    public String getTypeRoom() {
        return "Phòng thường";
    }
}
