package com.example.user.newproject;

public class MarkerTag {


    private String email;
    private String phoneNumber;
    private String catagory;
    private String lat;
    private String price;


    public MarkerTag() {

    }

    public String getcountry() {
        return email;
    }

    public void setcountry(String email) {
        this.email = email;
    }

    public String getarea() {
        return phoneNumber;
    }
    public String getprice() {
        return price;
    }


    public void setarea(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getcatagory() {
        return catagory;
    }

    public void setcatagory(String catagory) {
        this.catagory = catagory;
    }
    public void setprice(String price) {
        this.price= price;
    }

}