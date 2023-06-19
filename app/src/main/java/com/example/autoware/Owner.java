package com.example.autoware;

public class Owner {
    private String name = "";
    private String garagename = "";
    private String location = "";
    private String address = "";
    private String phone = "";
    private String email = "";

    public Owner(){}

    public Owner(String name, String garagename, String location, String address, String phone, String email) {
        this.name = name;
        this.garagename = garagename;
        this.location = location;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getGaragename() {
        return garagename;
    }

    public String getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
