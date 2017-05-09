package com.example.saurabh.parkhunt;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by saura on 18-02-2017.
 */
public class RegisterRequest extends StringRequest
{
    //private static final String REGISTER_REQUEST_URL="http://parkhunt.host22.com/Register.php";
    private static final String REGISTER_REQUEST_URL ="http://parkhunt.xyz/Register.php";
    private Map<String,String>params;

    public RegisterRequest(String name, String email, String password, Response.Listener<String> listener)
    {
        super(Method.POST,REGISTER_REQUEST_URL,listener,null);

        int userpoints=100;
        params=new HashMap<>();
        params.put("name",name);
        params.put("email",email);
        params.put("password",password);
        params.put("userpoints", String.valueOf(userpoints));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
