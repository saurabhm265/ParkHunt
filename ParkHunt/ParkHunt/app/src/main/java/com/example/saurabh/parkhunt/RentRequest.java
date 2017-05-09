package com.example.saurabh.parkhunt;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;



public class RentRequest extends StringRequest {
     private static final String RENT_REQUEST_URL="http://parkhunt.xyz/Rent.php";
    private Map<String, String> params;

    public RentRequest(Double rate, Double lat, Double lng, Response.Listener<String> listener){
        super(Method.POST,RENT_REQUEST_URL,listener,null);

        int tempUserid = ParkHuntUser.getUserId();
        params=new HashMap<>();
        params.put("rate", String.valueOf(rate));
        params.put("lat", String.valueOf(lat));
        params.put("lng", String.valueOf(lng));
        params.put("userid", String.valueOf(tempUserid));

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
