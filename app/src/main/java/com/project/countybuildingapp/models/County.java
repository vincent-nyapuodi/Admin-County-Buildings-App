package com.project.countybuildingapp.models;

public class County {

    private String name, email, countyname;
    private int phone;

    public County() {
    }

    public County(String name, String email, String countyname, int phone) {
        this.name = name;
        this.email = email;
        this.countyname = countyname;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountyname() {
        return countyname;
    }

    public void setCountyname(String countyname) {
        this.countyname = countyname;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }
}
