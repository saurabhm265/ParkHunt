package com.example.saurabh.parkhunt;

import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

/**
 * Created by User on 4/11/17.
 */

public  class ParkHuntUser {
    public static enum Color { Green, Red, Blue, Orange } ;
    static int userId ;
    static String name;
    static String email;
    static boolean canRefreshMap=true;
    static int userPoints;


    static ParkLotData selectedParkLot;

    public static ParkLotData getSelectedParkLot() {
        return selectedParkLot;
    }

    public static void setSelectedParkLot(ParkLotData selectedParkLot) {
        ParkHuntUser.selectedParkLot = selectedParkLot;
    }

    public static int getUserPoints() {
        return userPoints;
    }

    public static void setUserPoints(int userPoints) {
        ParkHuntUser.userPoints = userPoints;
    }

    public static boolean isCanRefreshMap() {
        return canRefreshMap;
    }

    public static void setCanRefreshMap(boolean canRefreshMap) {
        ParkHuntUser.canRefreshMap = canRefreshMap;
    }

    public static HashMap<Marker, ParkLotData> allMarkersMap = new HashMap<Marker, ParkLotData>();

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        ParkHuntUser.userId = userId;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        ParkHuntUser.name = name;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        ParkHuntUser.email = email;
    }


}
