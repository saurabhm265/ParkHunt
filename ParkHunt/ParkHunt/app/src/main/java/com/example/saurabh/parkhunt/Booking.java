package com.example.saurabh.parkhunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Booking extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button cancelBooking,confirmBooking;
    TextView showPerHourRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Intent intent=getIntent();

        int markerID =intent.getIntExtra("markerId",0);
        ParkLotData selectedParkLot = ParkHuntUser.getSelectedParkLot();
        if(selectedParkLot.getID() == markerID)
        {
            showPerHourRate=(TextView)findViewById(R.id.showPrHrRate);
            showPerHourRate.setText("$"+String.valueOf(selectedParkLot.getRate()));
        }
        else
        {

        }

           // Spinner element
      Spinner spinner = (Spinner) findViewById(R.id.spinner);

      // Spinner click listener
      spinner.setOnItemSelectedListener(this);

      // Spinner Drop down elements
      List<String> categories = new ArrayList<String>();
      categories.add("1 hour");
      categories.add("2 hours");
      categories.add("4 hours");
      categories.add("8 hours");

      // Creating adapter for spinner
      ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

      // Drop down layout style - list view with radio button
      dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

      // attaching data adapter to spinner
      spinner.setAdapter(dataAdapter);

        final String hour = spinner.getSelectedItem().toString();


        confirmBooking=(Button)findViewById(R.id.confirmBooking);
        confirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    confirmBooking(Integer.parseInt(String.valueOf(hour.charAt(0))));
            }
        });

        cancelBooking=(Button)findViewById(R.id.cancelBooking);
        cancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();    //finish Booking activity and go back to Map Activity
            }
        });


    }

    public void confirmBooking(int hours){

        ParkLotData selectedParkLot = ParkHuntUser.getSelectedParkLot();
        //Bookking req

        Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    Toast.makeText(getApplicationContext(), "Booking Confirmed!!!", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Booking.this);
                                    builder.setMessage("Booking Failed").setNegativeButton("Retry", null).create().show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    BookingRequest bookingRequest = new BookingRequest(hours, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Booking.this);
                    queue.add(bookingRequest);
                    ParkHuntUser.setSelectedParkLot(new ParkLotData());

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
