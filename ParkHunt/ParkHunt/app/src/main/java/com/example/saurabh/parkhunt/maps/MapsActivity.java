package com.example.saurabh.parkhunt.maps;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.saurabh.parkhunt.Account;
import com.example.saurabh.parkhunt.Booking;
import com.example.saurabh.parkhunt.Login;
import com.example.saurabh.parkhunt.ParkHuntUser;
import com.example.saurabh.parkhunt.ParkLotData;
import com.example.saurabh.parkhunt.R;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import android.support.v7.app.AppCompatActivity;

import com.example.saurabh.parkhunt.RentRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import android.os.Handler;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    View mapView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    private Handler mHandler = new Handler();
    boolean quit,onLongPressMarker=false;

    //Map activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        //        .findFragmentById(R.id.map);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout);

        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout, toolbar,R.string.open,R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        navigationView=(NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                int id=item.getItemId();
                switch(id)
                {
                    case R.id.nav_account:
                        Intent a=new Intent(MapsActivity.this, Account.class);
                        startActivity(a);
                        break;
                    case R.id.logout:
                        Intent e=new Intent(MapsActivity.this, Login.class);
                        startActivity(e);
                        break;
                }
                return false;
            }
        });

        quit = false;
         mHandler.postDelayed(new Runnable() {
            public void run () {
                if (! quit )
                    refreshMap();
            }
        }, 15000);
        // Starting locations retrieve task
        new RetrieveTask().execute();

    }

    //Refresh map after every after 30sc
    protected void refreshMap(){

        mHandler.postDelayed(new Runnable() {
            public void run () {
                refreshMap();
            }
        }, 15000);

        if (ParkHuntUser.isCanRefreshMap()){
            mMap.clear();
            new RetrieveTask().execute();
        }
    }

    Marker marker;

    //This method search for the location
    public void onSearch(View view) {
        EditText location_tf = (EditText) findViewById(R.id.TFaddress);
        String location = location_tf.getText().toString();
        List<Address> addressList = null;
        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);


            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            String locality = address.getLocality();
            Toast.makeText(this, locality, Toast.LENGTH_LONG).show();  //show location

            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            //setMarker(locality, latLng);
            goToLocationZoom(address.getLatitude(),address.getLongitude(),15);

        }
    }

    // Background task to retrieve locations from remote mysql server
    private class RetrieveTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String strUrl = "http://parkhunt.xyz/retrieve.php";
            URL url = null;
            StringBuffer sb = new StringBuffer();
            try {
                url = new URL(strUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream iStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));
                String line = "";
                while( (line = reader.readLine()) != null){
                    sb.append(line);
                }

                reader.close();
                iStream.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

     // Background thread to parse the JSON data retrieved from MySQL server
    private class ParserTask extends AsyncTask<String, Void, List<ParkLotData>>{
        @Override
        protected List<ParkLotData> doInBackground(String... params) {
            MarkerJSONParser markerParser = new MarkerJSONParser();
            JSONObject json = null;
            try {
                json = new JSONObject(params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            List<ParkLotData> markersList = markerParser.parse(json);
            return markersList;
        }

        @Override
        protected void onPostExecute(List<ParkLotData>  result) {
            for(int i=0; i<result.size();i++){
                ParkLotData dataTemp = result.get(i);
                setMarker(dataTemp);
            }
        }
    }

    public Drawable getDrawable(String name) {
    Context context = this.getApplicationContext();
    int resourceId = context.getResources().getIdentifier(name, "drawable", this.getApplicationContext().getPackageName());
    return context.getResources().getDrawable(resourceId);
    }
    public int getDrawableIdInt(String name) {
    Context context = this.getApplicationContext();
    return context.getResources().getIdentifier(name, "drawable", this.getApplicationContext().getPackageName());
    }


    //add marker
    protected void setMarker(ParkLotData lotData) {

            LatLng latLng = new LatLng(lotData.getLat(), lotData.getLng());
            int rateInt = Double.valueOf(lotData.getRate()).intValue();

            String name = lotData.getColor() + String.valueOf(rateInt);
            if(lotData.isBooked()) name = "booked";
            else if(lotData.getUserId()==ParkHuntUser.getUserId())
                name= "red" + String.valueOf(rateInt);
            BitmapDescriptor icon=null;

            icon = BitmapDescriptorFactory.fromResource(getDrawableIdInt(name));

            MarkerOptions options= new MarkerOptions()
                                    //.snippet("Rohit,$45/hr")
                                    //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                    .icon(icon)
                                    .draggable(true)
                                    .position(latLng);
            marker = mMap.addMarker(options);
            ParkHuntUser.allMarkersMap.put(marker, lotData);
     }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mMap != null) {

            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    Geocoder gc = new Geocoder(MapsActivity.this);
                    LatLng ll = marker.getPosition();
                    List<Address> list = null;
                    try {
                        list = gc.getFromLocation(ll.latitude, ll.longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Address add = list.get(0);
                    marker.setTitle(add.getLocality());
                    marker.showInfoWindow();
                }
            });

            //When Any marker click
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public boolean onMarkerClick(Marker marker) {
                ParkLotData dataLot = ParkHuntUser.allMarkersMap.get(marker);
                ParkHuntUser.setSelectedParkLot(dataLot);
                //check marker is clickable or not
                if(onLongPressMarker==true){
                    showAlertDialog(marker);
                }
                else {
                    Intent intentBooking = new Intent(MapsActivity.this, Booking.class);
                    intentBooking.putExtra("markerId",dataLot.getID());
                    startActivity(intentBooking);
                }


                return false;

            }


        });

            /*mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v =getLayoutInflater().inflate(R.layout.info_window,null);

                    TextView tvLocality = (TextView) v.findViewById(R.id.tv_locality);
                    TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
                    TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
                    TextView tvSnippet = (TextView) v.findViewById(R.id.tv_snippet);

                    LatLng ll = marker.getPosition();
                    tvLocality.setText(marker.getTitle());
                    tvLat.setText("Latitude: " + ll.latitude);
                    tvLng.setText("Longitude: " + ll.longitude);
                    tvSnippet.setText(marker.getSnippet());

                    return v;
                }
            });*/

        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom above zoomLayout
            View zoom_in_button = mapView.findViewWithTag("GoogleMapZoomInButton");
            View zoom_layout = (View) zoom_in_button.getParent();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ABOVE, zoom_layout.getId());
            layoutParams.setMargins(0, 0, 30, 30);
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
        //mMap.setOnInfoWindowClickListener(this);

        // Adding and showing marker while long press the GoogleMap
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            //@Override
            public void onMapLongClick(LatLng arg0) {
                // Clears any existing markers from the GoogleMap
                //mMap.clear();

                onLongPressMarker=true; //make maker clickable
                // Creating an instance of MarkerOptions to set position
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting position on the MarkerOptions
                markerOptions.position(arg0);

                // Animating to the currently touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));

                // Adding marker on the GoogleMap
                Marker marker = mMap.addMarker(markerOptions);

                // Showing InfoWindow on the GoogleMap
                marker.showInfoWindow();

            }
        });
    }

    private void goToLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        mMap.moveCamera(update);
    }

    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mMap.animateCamera(update);


    }


    private void showAlertDialog(final Marker marker) {

    AlertDialog.Builder alert = new AlertDialog.Builder(this);
    LayoutInflater inflater = this.getLayoutInflater();
    final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
    alert.setView(dialogView);

    final EditText edt = (EditText) dialogView.findViewById(R.id.etCustomDialog);

    //alert dialog

    alert.setTitle("Rent Your Place");
    alert.setIcon(R.drawable.parking_lot_icon);
    alert.setMessage("Enter amount below in multiple of 5(e.g 5,10,15)");
    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                    double rate=Double.parseDouble(edt.getText().toString());
                    LatLng ll = marker.getPosition();
                    double lat=ll.latitude;
                    double lng=ll.longitude;
                    saveMarker(rate,lat,lng);

                }
        });

            alert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            //pass
            }
         });

        AlertDialog b = alert.create();
        b.show();

    }

        public void saveMarker(double rate,double lat,double lng){
                    if(rate > 50)
                    {
                        rate = 50;
                    }
                    else if (rate % 5 != 0)
                    {
                        rate = rate - (rate % 5);
                    }
                    if(rate == 0) rate = 5;

                    Response.Listener<String> responseListener = new Response.Listener<String>(){
                      @Override
                        public void onResponse(String response) {
                          try {
                              JSONObject jsonResponse = new JSONObject(response);
                              boolean success = jsonResponse.getBoolean("success");

                              if (success){
                                  Toast.makeText(getApplicationContext(), "Parking spot put on rent!!!", Toast.LENGTH_SHORT).show();
                                  onLongPressMarker=false; //making marker unclickable
                              }
                          }
                          catch (Exception e){

                          }

                      }
                    };

                    RentRequest rentRequest=new RentRequest(rate,lat,lng,responseListener);
                    RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
                    queue.add(rentRequest);
    }


//    //place markers
//    @Override
//    public void onInfoWindowClick(Marker marker) {
//        Toast.makeText(this, "Info window clicked",
//                Toast.LENGTH_SHORT).show();
//
//
//    }

    //This menu is used for changing the map type(e.g. satellite or normal)
     @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId()) {
            case R.id.mapTypeNone:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.mapTypeNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;



            default:
                break;
        }
            return super.onOptionsItemSelected(item);
    }

    //for three lines in navigation drawer
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
        }

}


