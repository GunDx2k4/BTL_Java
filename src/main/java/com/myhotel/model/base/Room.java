package com.myhotel.model.base;

import java.io.Serializable;

public abstract class Room implements Serializable {
    private String roomID;
    private String roomName;
    private String typeOfBed;
    private float pricePerNight;
    private int floor;

    public Room() {
    }

    public Room(String roomID, String roomName, String typeOfBed, float pricePerNight, int floor) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.typeOfBed = typeOfBed;
        this.pricePerNight = pricePerNight;
        this.floor = floor;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getTypeOfBed() {
        return typeOfBed;
    }

    public void setTypeOfBed(String typeOfBed) {
        this.typeOfBed = typeOfBed;
    }

    public float getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(float pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public abstract String getTypeRoom();

}
