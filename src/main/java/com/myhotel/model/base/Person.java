package com.myhotel.model.base;

import java.io.Serializable;

public abstract class Person implements Serializable {
    private String name;
    private String phoneNumber;
    private String CCCD;

    public Person() {

    }

    public Person(String name, String phoneNumber, String CCCD) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.CCCD = CCCD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCCCD() {
        return CCCD;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }
}
