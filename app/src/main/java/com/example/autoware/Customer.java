package com.example.autoware;

public class Customer {
    private String name = "";
    private String location = "";
    private String email = "";
    public Customer() {
    }
    public Customer(String name, String location, String email) {
        this.name = name;
        this.location = location;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getEmail() {
        return email;
    }
}
