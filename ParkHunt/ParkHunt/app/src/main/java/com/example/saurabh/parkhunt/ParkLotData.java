package com.example.saurabh.parkhunt;

/**
 * Created by User on 4/11/17.
 */
 public class ParkLotData {
        int ID;
        int UserId;
        double rate;
        double lat;
        double lng;
        boolean isBooked;
        String color;

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public String getColor() {
        return color;
    }

    public ParkLotData()
    {
        if(this.UserId == ParkHuntUser.getUserId())
        {
            color = "red";
        }
        else
        {
            color = "green";
        }

        if(this.isBooked == true)
        {
            color = "blue";
        }

        this.color = color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public  double getRate() {
        return rate;
    }

    public  void setRate(double rate) {
        this.rate = rate;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}