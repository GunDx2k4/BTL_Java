package com.myhotel.model;

import com.myhotel.Main;
import com.myhotel.model.base.Person;

import java.io.Serializable;

public class Staff extends Person implements Serializable {
    private String staffID;
    private String position;
    private float salaryRatio;

    public Staff() {

    }

    public Staff(String name, String phoneNumber, String CCCD, float salaryRatio) {
        super(name, phoneNumber, CCCD);
        this.salaryRatio = salaryRatio;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public float getSalaryRatio() {
        return salaryRatio;
    }

    public void setSalaryRatio(float salaryRatio) {
        this.salaryRatio = salaryRatio;
    }

    public float getSalary() {
        return salaryRatio * Main.SALARY_BASE;
    }
}
