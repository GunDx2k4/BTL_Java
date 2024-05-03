package com.myhotel.model;

import com.myhotel.model.base.Room;

import java.io.Serializable;

public class SuiteRoom extends Room implements Serializable {
    private boolean hasLivingRoom;
    private boolean hasJacuzzi;
    private boolean hasMiniBar;

    public SuiteRoom() {

    }
    public SuiteRoom(String roomID, String roomName, String typeOfBed, float pricePerNight, int floor, boolean hasLivingRoom, boolean hasJacuzzi, boolean hasMiniBar) {
        super(roomID, roomName, typeOfBed, pricePerNight, floor);
        this.hasLivingRoom = hasLivingRoom;
        this.hasJacuzzi = hasJacuzzi;
        this.hasMiniBar = hasMiniBar;
    }

    public boolean isHasJacuzzi() {
        return hasJacuzzi;
    }

    public void setHasJacuzzi(boolean hasJacuzzi) {
        this.hasJacuzzi = hasJacuzzi;
    }

    public boolean isHasMiniBar() {
        return hasMiniBar;
    }

    public void setHasMiniBar(boolean hasMiniBar) {
        this.hasMiniBar = hasMiniBar;
    }

    public boolean isHasLivingRoom() {
        return hasLivingRoom;
    }

    public void setHasLivingRoom(boolean hasLivingRoom) {
        this.hasLivingRoom = hasLivingRoom;
    }

    @Override
    public String getTypeRoom() {
        return "Phòng cao cấp";
    }
}
