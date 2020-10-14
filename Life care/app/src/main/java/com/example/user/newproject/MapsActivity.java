package com.example.user.newproject;



import android.*;
import android.Manifest;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.view.Gravity.CENTER;

/**
 * Created by User on 10/2/2017.
 */

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String URL="http://192.168.0.101/project/hospital.php";
    public static final String URL_m="http://192.168.0.101/project/ambulance.php";
    private JSONArray result;
    TextView textView;

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }


            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);


        }




        Button ambulance = (Button) findViewById(R.id.B_Ambulance);
        ambulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v)
            {
                mMap.clear();
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.ambulance_view, null);

                textView=(TextView) popupView.findViewById(R.id.button2);
                TextView textView1=(TextView) popupView.findViewById(R.id.button3);





                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.MATCH_PARENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();


                        return true;
                    }
                });



                //from here start hospital show
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());


                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_m, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("JSONResult", response.toString());
                        JSONObject j = null;
                        try {

                            j = new JSONObject(response);
                            result = j.getJSONArray("FL");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject jsonObject1 = result.getJSONObject(i);
                                final String lat_i = jsonObject1.getString("latitude");
                                final String long_i = jsonObject1.getString("longitude");
                                final String phone = jsonObject1.getString("contact_number");
                                final String ambulance_title = jsonObject1.getString("ambulance_name");
                                final  String a_catagory=jsonObject1.getString("catagory");
                                final  String a_price=jsonObject1.getString("price");
                                final  String a_area=jsonObject1.getString("area");
                                final  String a_country=jsonObject1.getString("country");







                                Log.d(TAG, "getDeviceLocation: getting the devices current location");

                                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);

                                try{
                                    if(mLocationPermissionsGranted){

                                        final Task location = mFusedLocationProviderClient.getLastLocation();
                                        location.addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                //23.7030749,90.4405513
                                                if(task.isSuccessful())
                                                {
                                                    Log.d(TAG, "onComplete: found location!");
                                                    Location currentLocation = (Location) task.getResult();


                                                    LatLng latLng=new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());

                                                    Location selected_location=new Location("locationA");
                                                    selected_location.setLatitude(currentLocation.getLatitude());
                                                    selected_location.setLongitude(currentLocation.getLongitude());

                                                    Location near_locations=new Location("locationB");
                                                    near_locations.setLatitude(Double.parseDouble(lat_i));
                                                    near_locations.setLongitude(Double.parseDouble(long_i));
                                                    double distance=selected_location.distanceTo(near_locations);
                                                    moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                                            12f);
                                                    //Toast.makeText(MapsActivity.this,Double.toString(distance), Toast.LENGTH_LONG).show();


                                                    if (distance<=8000) {
                                                        MarkerTag yourMarkerTag = new MarkerTag();
                                                        yourMarkerTag.setcountry(a_country);
                                                        yourMarkerTag.setarea(a_area);
                                                        yourMarkerTag.setcatagory(a_catagory);
                                                        yourMarkerTag.setprice(a_price);





                                                        MarkerOptions marker = new MarkerOptions()
                                                                .position(new LatLng(near_locations.getLatitude(), near_locations.getLongitude()))
                                                                .title(ambulance_title)
                                                                .snippet(phone)
                                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ambulance));
                                                   /* mMap.addCircle(new CircleOptions()
                                                            .center(new LatLng(Double.parseDouble(lat_i), Double.parseDouble(long_i)))
                                                            .radius(20)
                                                            .strokeColor(Color.WHITE));*/

                                                        mMap.addCircle(new CircleOptions()
                                                                .center(new LatLng(Double.parseDouble(lat_i), Double.parseDouble(long_i)))
                                                                .radius(30)
                                                                .strokeColor(Color.WHITE));
                                                        Marker marker1 = mMap.addMarker(marker);
                                                        marker1.setTag(yourMarkerTag);
                                                        marker1.showInfoWindow();





                                                        //Toast.makeText(MapsActivity.this,Double.toString(distance)+ambulance_title+" "+"lat:"+lat_i+",long:"+long_i, Toast.LENGTH_LONG).show();

                                                        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
                                                            @Override
                                                            public void onInfoWindowLongClick(Marker marker) {
                                                                MarkerTag yourMarkerTag = ((MarkerTag) marker.getTag());

                                                                String title = marker.getTitle();
                                                                String area = yourMarkerTag.getarea();
                                                                String country = yourMarkerTag.getcountry();
                                                                String am_catagory = yourMarkerTag.getcatagory();
                                                                String am_price = yourMarkerTag.getprice();






                                                                // Check if a click count was set, then display the click count.
                                                                if (marker != null) {


                                                                    Intent intent = new Intent(MapsActivity.this, Ambulance_details.class);
                                                                    intent.putExtra("ambulance_name", ambulance_title);
                                                                    intent.putExtra("ambulance_area",area);
                                                                    intent.putExtra("ambulance_country", country);
                                                                    intent.putExtra("ambulance_price", am_price);
                                                                    intent.putExtra("ambulance_catagory", am_catagory);




                                                                    startActivity(intent);

                                                                }

                                                            }
                                                        });

                                                      /*  mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener()
                                                        {
                                                            @Override
                                                            public void onInfoWindowLongClick(Marker marker) {
                                                                MarkerTag yourMarkerTag = ((MarkerTag) marker.getTag());

                                                                String title = marker.getTitle();
                                                                String area = yourMarkerTag.getarea();
                                                                String country = yourMarkerTag.getcountry();
                                                                String catagory = yourMarkerTag.getcatagory();


                                                                // Check if a click count was set, then display the click count.
                                                                if (marker != null) {


                                                                    Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                                                                    intent.putExtra("hospital_name", title);
                                                                    intent.putExtra("hospital_area", area);
                                                                    intent.putExtra("hospital_country", country);
                                                                    intent.putExtra("hospital_catagory", catagory);

                                                                    startActivity(intent);

                                                                }

                                                            }
                                                        });*/







                                                    }



                                                   // Toast.makeText(MapsActivity.this,Double.toString(distance), Toast.LENGTH_LONG).show();



                                                }else{
                                                    Log.d(TAG, "onComplete: current location is null");
                                                    Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                }catch (SecurityException e){
                                    Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
                                }
















                             /*   MarkerOptions marker = new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble(lat_i), Double.parseDouble(long_i)))
                                        .title("Ambulance")
                                        .snippet(phone)
                                        .icon(BitmapDescriptorFactory
                                                .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                                mMap.addMarker(marker);

                       /*marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(lat_i) , Double.parseDouble(long_i)))
                                .title(Double.valueOf(lat_i).toString() + "," + Double.valueOf(long_i).toString())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))

                        );*/






                            }


                        } catch (NullPointerException e) {
                            e.printStackTrace();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(MapsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


                int socketTimeout = 10000;
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                requestQueue.add(stringRequest);



            }




        });

        Button hospital =(Button)findViewById(R.id.hospital);
        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                mMap.clear();
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.ambulance_view, null);

                textView=(TextView) popupView.findViewById(R.id.button2);
                TextView textView1=(TextView) popupView.findViewById(R.id.button3);





                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.MATCH_PARENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();


                        return true;
                    }
                });



                //from here start hospital show
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());


                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("JSONResult", response.toString());

                        JSONObject j = null;
                        try {

                            j = new JSONObject(response);
                            result = j.getJSONArray("FL");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject jsonObject1 = result.getJSONObject(i);
                                final String lat_i = jsonObject1.getString("latitude");
                                final String long_i = jsonObject1.getString("longitude");
                                final String phone = jsonObject1.getString("contact_number");
                                final String hoapital_title = jsonObject1.getString("Hospital_name");
                                final String h_area=jsonObject1.getString("area");
                                final String h_country=jsonObject1.getString("country");
                                final String h_catagory=jsonObject1.getString("catagory");



                                Log.d(TAG, "getDeviceLocation: getting the devices current location");

                                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);

                                try {
                                    if (mLocationPermissionsGranted) {

                                        final Task location = mFusedLocationProviderClient.getLastLocation();
                                        location.addOnCompleteListener(new OnCompleteListener()
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task task)
                                            {
                                                //23.7030749,90.4405513
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "onComplete: found location!");
                                                    Location currentLocation = (Location) task.getResult();


                                                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                                                    Location selected_location = new Location("locationA");
                                                    selected_location.setLatitude(currentLocation.getLatitude());
                                                    selected_location.setLongitude(currentLocation.getLongitude());

                                                    Location near_locations = new Location("locationB");
                                                    near_locations.setLatitude(Double.parseDouble(lat_i));
                                                    near_locations.setLongitude(Double.parseDouble(long_i));
                                                    // Toast.makeText(MapsActivity.this, lat_i+","+long_i, Toast.LENGTH_LONG).show();

                                                    double distance = selected_location.distanceTo(near_locations);
                                                    /*mMap.addCircle(new CircleOptions()
                                                           .center(latLng)
                                                           // .center(new LatLng(23.7574986,90.4290558))

                                                            .radius(230)
                                                            .strokeColor(Color.argb(15,128, 155, 255))
                                                            .strokeWidth(3)
                                                            .fillColor(Color.argb(5,128, 155, 255)));
                                                    moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                                            10f);*/

                                                    mMap.addCircle(new CircleOptions()
                                                            .center(latLng)
                                                            .radius(200)
                                                            .strokeColor(Color.argb(40,75, 100, 191))
                                                            .strokeWidth(4)
                                                            .fillColor(Color.argb(40,80, 134, 200)));
                                                    moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                                            13f);


                                                    if (distance <= 5000)
                                                    {
                                                        MarkerTag yourMarkerTag = new MarkerTag();
                                                        yourMarkerTag.setcountry(h_country);
                                                        yourMarkerTag.setarea(h_area);
                                                        yourMarkerTag.setcatagory(h_catagory);


                                                        MarkerOptions marker = new MarkerOptions()
                                                                .position(new LatLng(near_locations.getLatitude(), near_locations.getLongitude()))
                                                                .title(hoapital_title)
                                                                .snippet(phone)
                                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital));
                                                        //
                                                        // .icon(BitmapDescriptorFactory
                                                        // .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                                                        Marker marker1 = mMap.addMarker(marker);
                                                        marker1.setTag(yourMarkerTag);
                                                        marker1.showInfoWindow();
                                                       // Toast.makeText(MapsActivity.this,Double.toString(distance)+hoapital_title+" "+"lat:"+lat_i+",long:"+long_i, Toast.LENGTH_LONG).show();


                                                        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
                                                            @Override
                                                            public void onInfoWindowLongClick(Marker marker) {
                                                                MarkerTag yourMarkerTag = ((MarkerTag) marker.getTag());

                                                                String title = marker.getTitle();
                                                                String area = yourMarkerTag.getarea();
                                                                String country = yourMarkerTag.getcountry();
                                                                String catagory = yourMarkerTag.getcatagory();


                                                                // Check if a click count was set, then display the click count.
                                                                if (marker != null) {


                                                                    Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                                                                    intent.putExtra("hospital_name", title);
                                                                    intent.putExtra("hospital_area", area);
                                                                    intent.putExtra("hospital_country", country);
                                                                    intent.putExtra("hospital_catagory", catagory);

                                                                    startActivity(intent);

                                                                }

                                                            }
                                                        });

                                                        /*mMap.addCircle(new CircleOptions()
                                                                .center(new LatLng(Double.parseDouble(lat_i), Double.parseDouble(long_i)))
                                                                .radius(20)
                                                                .strokeColor(Color.WHITE));*/
                                                   }



                                                   // Toast.makeText(MapsActivity.this, Double.toString(distance), Toast.LENGTH_LONG).show();


                                                }
                                                else {
                                                    Log.d(TAG, "onComplete: current location is null");
                                                    Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                } catch (SecurityException e) {
                                    Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
                                }











                               /* MarkerOptions marker = new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble(lat_i), Double.parseDouble(long_i)))
                                        .title(String.valueOf(i))
                                        .snippet("01990915644")
                                        .icon(BitmapDescriptorFactory
                                                .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                                mMap.addMarker(marker);*/

                       /*marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(lat_i) , Double.parseDouble(long_i)))
                                .title(Double.valueOf(lat_i).toString() + "," + Double.valueOf(long_i).toString())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))

                        );

                                mMap.addCircle(new CircleOptions()
                                        .center(new LatLng(Double.parseDouble(lat_i), Double.parseDouble(long_i)))
                                        .radius(30)
                                        .strokeColor(Color.GRAY));*/


                            }

                        } catch (NullPointerException e) {
                            e.printStackTrace();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(MapsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


                int socketTimeout = 10000;
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                requestQueue.add(stringRequest);



            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            public void onInfoWindowClick(Marker arg0) {

                if (arg0 != null ) {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+arg0.getSnippet()));
                    startActivity(intent);
                }




            }
        });


    }







    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 16f;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;

    private FusedLocationProviderClient mFusedLocationProviderClient;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // for hiding title

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_maps);

        getLocationPermission();
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                           Location currentLocation = (Location) task.getResult();



                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    16f);









                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
       /* mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(200)
                .strokeColor(Color.argb(40,75, 100, 191))
                .strokeWidth(4)
                .fillColor(Color.argb(40,101, 134, 255)));*/






    }

    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup, null);

        TextView textView=popupView.findViewById(R.id.doctor);
        textView.setText("");


        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }




    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapsActivity.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }


    public void onBackPressed()
    {

        final AlertDialog.Builder builder=new AlertDialog.Builder(MapsActivity.this);

        builder.setCancelable(true);
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);

        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }




}
