package com.example.saurabh.parkhunt;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 4/15/17.
 */

public class BookingRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL ="http://parkhunt.xyz/Booking.php";
    private Map<String,String> params;

    public BookingRequest(int hours, Response.Listener<String> listener)
    {
        super(Method.POST,REGISTER_REQUEST_URL,listener,null);

        int userpoints=100;
        params=new HashMap<>();
        params.put("userid", String.valueOf(ParkHuntUser.selectedParkLot.getUserId()));
        params.put("lotid",String.valueOf(ParkHuntUser.selectedParkLot.getID()));
        params.put("rate",String.valueOf(ParkHuntUser.selectedParkLot.getRate()));
        params.put("hours", String.valueOf(hours));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
