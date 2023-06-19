package com.example.autoware;

public class Car {
    private String registrationNumber;
    private String customerid;
    private String brand;
    private String model;
    private String fueltype;

    public Car() {
    }

    public Car(String registrationNumber, String customerId, String brand, String model, String fuelType) {
        this.registrationNumber = registrationNumber;
        this.customerid = customerId;
        this.brand = brand;
        this.model = model;
        this.fueltype = fuelType;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFueltype() {
        return fueltype;
    }

    public void setFueltype(String fueltype) {
        this.fueltype = fueltype;
    }
}
