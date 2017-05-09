package com.example.saurabh.parkhunt.maps;

import com.example.saurabh.parkhunt.ParkHuntUser;
import com.example.saurabh.parkhunt.ParkLotData;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MarkerJSONParser {



    public List<ParkLotData>  getMarkers(JSONArray jMarkers){
        int markersCount = jMarkers.length();
        List<ParkLotData> markersList = new ArrayList<>();
        /** Taking each marker, parses and adds to list object */
        for(int i=0; i<markersCount;i++){
            try {
                /** Call getMarker with marker JSON object to parse the marker */
                 ParkLotData parklotTemp = getMarkerGlobal((JSONObject)jMarkers.get(i));
                 markersList.add(parklotTemp);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return markersList;
    }


    /** Parsing the Marker JSON object */
    private ParkLotData getMarkerGlobal(JSONObject jMarker){

        ParkLotData marker = new ParkLotData();

        try {
            // Extracting latitude, if available
            if(!jMarker.isNull("ID")){
                marker.setID(jMarker.getInt("ID"));
            }

            // Extracting longitude, if available
            if(!jMarker.isNull("UserId")){
                marker.setUserId(jMarker.getInt("UserId"));
            }

            // Extracting latitude, if available
            if(!jMarker.isNull("rate")){
                marker.setRate(jMarker.getDouble("rate"));
            }

            // Extracting latitude, if available
            if(!jMarker.isNull("lat")){
                marker.setLat(jMarker.getDouble("lat"));
            }

            // Extracting longitude, if available
            if(!jMarker.isNull("lng")){
                marker.setLng(jMarker.getDouble("lng"));
            }

            if (!jMarker.isNull("IsBooked")){
                if(jMarker.getString("IsBooked").equals("1"))
                    marker.setBooked(true);
                else
                    marker.setBooked(false);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return marker;
    }


    // Receives a JSONObject and returns a list
    public List<ParkLotData> parse(JSONObject jObject){

        JSONArray jMarkers=null;

       try {
            //Retrieves all the elements in the 'markers' array *//*
             jMarkers = jObject.getJSONArray("markers");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Invoking getMarkers with the array of json object
        //where each json object represent a marker

        return getMarkers(jMarkers);
    }

    private List<HashMap<String, String>> getMarkersOld(JSONArray jMarkers){
        int markersCount = jMarkers.length();
        List<HashMap<String, String>> markersList = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> marker = null;

        //** Taking each marker, parses and adds to list object *//*
        for(int i=0; i<markersCount;i++){
            try {
                //** Call getMarker with marker JSON object to parse the marker *//*
                marker = getMarker((JSONObject)jMarkers.get(i));
                markersList.add(marker);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return markersList;
    }

    //** Parsing the Marker JSON object *//*
    private HashMap<String, String> getMarker(JSONObject jMarker){

        HashMap<String, String> marker = new HashMap<String, String>();
        String lat = "-NA-";
        String lng ="-NA-";

        try {
            // Extracting latitude, if available
            if(!jMarker.isNull("lat")){
                lat = jMarker.getString("lat");
            }

            // Extracting longitude, if available
            if(!jMarker.isNull("lng")){
                lng = jMarker.getString("lng");
            }

            marker.put("lat", lat);
            marker.put("lng", lng);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return marker;
    }
}
