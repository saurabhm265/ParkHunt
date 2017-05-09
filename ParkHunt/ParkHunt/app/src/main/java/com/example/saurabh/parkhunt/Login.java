package com.example.saurabh.parkhunt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.HandlerThread;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.*;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.saurabh.parkhunt.maps.MapsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Handler;
/*
*  class UserInSession {
*   int ID, string Name, string Password, bool iSLoggedIn,
*   Parklot parklotUser ; bool isDataDirty = false;
*  }
*
*  class ParkLots {
*
*  }
*
*
* */
public class Login extends AppCompatActivity
{


    public boolean flag=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText TFemail=(EditText)findViewById(R.id.TFemail);
        final EditText TFpassword=(EditText)findViewById(R.id.TFpassword);


        Button NewReg = (Button) findViewById(R.id.NewReg);
        NewReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newreg= new Intent(Login.this,Register.class);
                startActivity(newreg);
            }
        });

        Button Butlogin = (Button) findViewById(R.id.Butlogin);
        Butlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = TFemail.getText().toString();
                final String password = TFpassword.getText().toString();


               final ProgressDialog mProgressDialog = new ProgressDialog(Login.this);
                mProgressDialog.setProgress(0);
                mProgressDialog.setMax(100);
                mProgressDialog.setMessage("Loading map");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        int progress=0;
                        while (progress<=100){
                            try{
                                mProgressDialog.setProgress(progress);
                                progress++;
                                Thread.sleep(5);

                            }
                            catch (Exception e){


                            }
                        }

                        mProgressDialog.dismiss();

                        Login.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                }).start();





                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            ParkHuntUser.setUserId(jsonResponse.getInt("userid"));
                            ParkHuntUser.setEmail(jsonResponse.getString("email"));
                            ParkHuntUser.setName(jsonResponse.getString("name"));
                            ParkHuntUser.setUserPoints(jsonResponse.getInt("userpoints"));

                            if (success)
                            {

                                Toast.makeText(Login.this,"You have successfully log in",Toast.LENGTH_SHORT).show();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();

                                    Intent intent = new Intent(Login.this, MapsActivity.class);
                                    Login.this.startActivity(intent);


                                //startActivity(intent);
                            } else {
                                Toast.makeText(Login.this,"Can't login,server down",Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                builder.setMessage("Login Failed").setNegativeButton("Retry", null).create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(email, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Login.this);
                queue.add(loginRequest);


            }


        });
    }
}
