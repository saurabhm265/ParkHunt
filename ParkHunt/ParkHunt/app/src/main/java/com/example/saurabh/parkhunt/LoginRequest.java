package com.example.saurabh.parkhunt;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;


public class LoginRequest extends StringRequest
{
    private static final int CONNECTION_TIMEOUT = 15000;
    //private static final String LOGIN_REQUEST_URL="http://parkhunt.host22.com/Login.php";
    private static final String LOGIN_REQUEST_URL="http://parkhunt.xyz/Login.php";
    private Map<String,String> params;

    public LoginRequest(String email, String password, Response.Listener<String> listener)
    {
        super(Method.POST,LOGIN_REQUEST_URL,listener,null);
        params=new HashMap<>();
        params.put("email",email);
        params.put("password",password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
