package com.example.autoware;

//import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

public class Services {
    private String customerID;
    private String serviceID;
    private String ownerID;
    private List<String> cars;
    private List<String> Spareparts;
    private List<Integer> Sparepartsprice;
    private Boolean towingFlag;
    private String date;
    private Boolean status;
    private String description;
    private float amount;
    public Services() {
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public List<String> getSpareparts() {
        return Spareparts;
    }

    public void setSpareparts(List<String> spareparts) {
        Spareparts = spareparts;
    }

    public List<Integer> getSparepartsprice() {
        return Sparepartsprice;
    }

    public void setSparepartsprice(List<Integer> sparepartsprice) {
        Sparepartsprice = sparepartsprice;
    }

    public Services(String customerID, String serviceID, String ownerID, List<String> cars, Boolean towingFlag, String date, Boolean status, String description) {
        this.customerID = customerID;
        this.serviceID = serviceID;
        this.ownerID = ownerID;
        this.cars = cars;
        this.towingFlag = towingFlag;
        this.date = date;
        this.status = status;
        this.description = description;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<String> getCars() {
        return cars;
    }

    public void setCars(List<String> cars) {
        this.cars = cars;
    }

    public Boolean getTowingFlag() {
        return towingFlag;
    }

    public void setTowingFlag(Boolean towingFlag) {
        this.towingFlag = towingFlag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
