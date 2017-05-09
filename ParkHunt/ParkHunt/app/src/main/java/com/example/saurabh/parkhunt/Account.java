package com.example.saurabh.parkhunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        final TextView acnameText=(TextView) findViewById(R.id.acnameText);
        final TextView acEmailText=(TextView) findViewById(R.id.acEmailText);
        final TextView acPointsText=(TextView) findViewById(R.id.tvPointstext);

        Intent intent=getIntent();
        //Get From Database


        //String points=intent.getStringExtra("userpoint");
        String name=intent.getStringExtra("name");
        String email=intent.getStringExtra("email");
        name = ParkHuntUser.getName();
        email = ParkHuntUser.getEmail();
        acPointsText.setText("$"+String.valueOf(ParkHuntUser.getUserPoints()));
        acnameText.setText(name);
        acEmailText.setText(email);
    }
}

